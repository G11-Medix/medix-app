package com.example.medix.presentation.viewmodels.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medix.data.dto.CreatePacienteRequest
import com.example.medix.domain.repositories.AuthRepository
import com.example.medix.domain.repositories.PacienteRepository
import com.example.medix.presentation.viewmodels.status.AuthNavigationTarget
import com.example.medix.presentation.viewmodels.status.AuthUiState
import com.example.medix.presentation.viewmodels.status.PacienteFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val pacienteRepository: PacienteRepository,
) : ViewModel() {
    companion object {
        private const val TAG = "AuthViewModel"
    }

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun updatePhone(phone: String) {
        _uiState.update {
            it.copy(
                phone = phone,
                pacienteForm = it.pacienteForm.copy(telefono = phone),
                otpCode = "",
                otpSent = false,
                errorMessage = null,
                infoMessage = null,
            )
        }
    }

    fun updateOtpCode(code: String) {
        _uiState.update { it.copy(otpCode = code, errorMessage = null) }
    }

    fun updatePacienteForm(transform: (PacienteFormState) -> PacienteFormState) {
        _uiState.update {
            it.copy(
                pacienteForm = transform(it.pacienteForm),
                errorMessage = null,
            )
        }
    }

    fun requestOtp() {
        val normalizedPhone = normalizePhone(_uiState.value.phone)
        if (normalizedPhone.isBlank()) {
            showError("Ingresa un teléfono en formato internacional, por ejemplo +573001234567.")
            return
        }

        viewModelScope.launch {
            setLoading(true)
            runCatching {
                val eligibility = pacienteRepository.getAuthEligibilityByTelefono(normalizedPhone)
                    ?: error("No se pudo validar el acceso para este numero.")

                if (!eligibility.authorized) {
                    error(
                        when (eligibility.reason) {
                            "PATIENT_NOT_FOUND" -> "Este numero no esta autorizado para ingresar."
                            "PATIENT_INACTIVE" -> "Este paciente no tiene acceso activo."
                            "USER_NOT_LINKED" -> "Este paciente aun no tiene acceso habilitado. Contacta al administrador."
                            "USER_INACTIVE" -> "Este paciente no tiene acceso activo."
                            "AUTH_USER_NOT_FOUND" -> "El acceso de este paciente no esta completamente configurado."
                            "PHONE_MISMATCH" -> "El telefono registrado no coincide con el habilitado para acceso."
                            else -> "No se pudo validar el acceso para este numero."
                        }
                    )
                }

                when {
                    eligibility.paciente == null ->
                        error("No se recibieron los datos del paciente autorizado.")
                    eligibility.paciente.estado != "ACTIVO" ->
                        error("Este paciente no tiene acceso activo.")
                    eligibility.paciente.id_usuario.isNullOrBlank() ->
                        error("Este paciente aun no tiene acceso habilitado. Contacta al administrador.")
                }

                authRepository.requestOtp(normalizedPhone)
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        phone = normalizedPhone,
                        otpSent = true,
                        infoMessage = "Te enviamos un codigo por SMS.",
                        isLoading = false,
                    )
                }
            }.onFailure {
                Log.e(
                    TAG,
                    "requestOtp failed for phone=$normalizedPhone: ${it.message}",
                    it,
                )
                showError("No se pudo enviar el código de verificación: ${it.localizedMessage}")
            }
        }
    }

    fun verifyOtpAndResolveFlow() {
        val normalizedPhone = normalizePhone(_uiState.value.phone)
        val otpCode = _uiState.value.otpCode.trim()

        if (normalizedPhone.isBlank()) {
            showError("Ingresa un teléfono válido antes de verificar.")
            return
        }
        if (otpCode.length < 4) {
            showError("Ingresa el código OTP completo.")
            return
        }

        viewModelScope.launch {
            setLoading(true)
            runCatching {
                authRepository.verifyOtp(normalizedPhone, otpCode)
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        infoMessage = null,
                        errorMessage = null,
                        navigationTarget = AuthNavigationTarget.SCHEDULE,
                        pacienteForm = it.pacienteForm.copy(telefono = normalizedPhone),
                    )
                }
            }.onFailure {
                Log.e(
                    TAG,
                    "verifyOtp failed for phone=$normalizedPhone: ${it.message}",
                    it,
                )
                showError("No se pudo verificar el código: ${it.localizedMessage}")
            }
        }
    }

    fun registerPaciente() {
        val form = _uiState.value.pacienteForm
        val normalizedPhone = normalizePhone(form.telefono)
        val currentUserId = authRepository.currentUserId()

        val validationError = validatePacienteForm(form, normalizedPhone)
        if (validationError != null) {
            showError(validationError)
            return
        }

        viewModelScope.launch {
            setLoading(true)
            runCatching {
                pacienteRepository.createPaciente(
                    CreatePacienteRequest(
                        tipoDocumento = form.tipoDocumento,
                        numeroDocumento = form.numeroDocumento.trim(),
                        nombres = form.nombres.trim(),
                        apellidos = form.apellidos.trim(),
                        fechaNacimiento = form.fechaNacimiento.trim(),
                        telefono = normalizedPhone,
                        correo = form.correo.trim().ifBlank { null },
                        estado = form.estado,
                        idUsuario = currentUserId,
                        idInstitucion = form.idInstitucion.trim().toLong(),
                    )
                )
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        infoMessage = "Paciente creado correctamente.",
                        navigationTarget = AuthNavigationTarget.SCHEDULE,
                    )
                }
            }.onFailure {
                showError("No se pudo crear el paciente: ${it.localizedMessage}")
            }
        }
    }

    fun clearTransientMessages() {
        _uiState.update { it.copy(errorMessage = null, infoMessage = null) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationTarget = AuthNavigationTarget.NONE) }
    }

    private fun validatePacienteForm(
        form: PacienteFormState,
        normalizedPhone: String,
    ): String? {
        return when {
            form.tipoDocumento.isBlank() -> "Selecciona el tipo de documento."
            form.numeroDocumento.isBlank() -> "Ingresa el número de documento."
            form.nombres.isBlank() -> "Ingresa los nombres."
            form.apellidos.isBlank() -> "Ingresa los apellidos."
            form.fechaNacimiento.isBlank() -> "Ingresa la fecha de nacimiento en formato YYYY-MM-DD."
            normalizedPhone.isBlank() -> "Ingresa el teléfono del paciente."
            form.estado.isBlank() -> "Selecciona el estado del paciente."
            form.idInstitucion.isBlank() -> "Ingresa el id de institución."
            form.idInstitucion.toLongOrNull() == null -> "El id de institución debe ser numérico."
            else -> null
        }
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = isLoading,
                errorMessage = null,
            )
        }
    }

    private fun showError(message: String) {
        _uiState.update {
            it.copy(
                isLoading = false,
                errorMessage = message,
            )
        }
    }

    private fun normalizePhone(phone: String): String {
        return phone.filterNot(Char::isWhitespace)
    }
}
