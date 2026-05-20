package com.example.medix.presentation.viewmodels.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medix.core.auth.SessionManager
import com.example.medix.core.phone.CountryDialCodes
import com.example.medix.data.dto.CreatePacienteRequest
import com.example.medix.data.dto.EpsDto
import com.example.medix.data.sources.local.TokenDataStore
import com.example.medix.domain.repositories.AuthRepository
import com.example.medix.domain.repositories.DeviceRepository
import com.example.medix.domain.repositories.LegalRepository
import com.example.medix.domain.repositories.PacienteRepository
import com.example.medix.presentation.viewmodels.status.AuthNavigationTarget
import com.example.medix.presentation.viewmodels.status.AuthUiState
import com.example.medix.presentation.viewmodels.status.PacienteFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val pacienteRepository: PacienteRepository,
    private val legalRepository: LegalRepository,
    private val sessionManager: SessionManager,
    private val tokenStorage: TokenDataStore,
    private val deviceRepository: DeviceRepository
) : ViewModel() {
    companion object {
        private const val TAG = "AuthViewModel"
    }

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun updatePhone(phone: String) {
        val phoneDigits = phone.filter(Char::isDigit)
        _uiState.update {
            it.copy(
                phone = phoneDigits,
                pacienteForm = it.pacienteForm.copy(telefono = phoneDigits),
                otpCode = "",
                otpSent = false,
                errorMessage = null,
                infoMessage = null,
            )
        }
    }

    fun updatePhoneCountryCode(code: String) {
        val normalizedCode = CountryDialCodes.sanitizeDialCode(code)
        _uiState.update {
            it.copy(
                phoneCountryCode = normalizedCode,
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

    fun loadEpsIfNeeded() {
        if (_uiState.value.epsOptions.isNotEmpty() || _uiState.value.isLoadingEps) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingEps = true, errorMessage = null) }

            runCatching {
                pacienteRepository.getEps().sortedBy(EpsDto::nombre)
            }.onSuccess { epsOptions ->
                _uiState.update {
                    it.copy(
                        isLoadingEps = false,
                        epsOptions = epsOptions,
                    )
                }
            }.onFailure {
                showError("No se pudo cargar la lista de EPS: ${it.localizedMessage}")
                _uiState.update { state -> state.copy(isLoadingEps = false) }
            }
        }
    }

    fun requestOtp() {
        val normalizedPhone = buildInternationalPhone(
            countryCode = _uiState.value.phoneCountryCode,
            nationalNumber = _uiState.value.phone,
        )
        if (normalizedPhone.isBlank()) {
            showError("Ingresa un teléfono válido para continuar.")
            return
        }

        sendLoginOtp(normalizedPhone, isResend = false)
    }

    fun resetAuthState() {
        _uiState.value = AuthUiState()
    }

    fun resendLoginOtp() {
        val normalizedPhone = buildInternationalPhone(
            countryCode = _uiState.value.phoneCountryCode,
            nationalNumber = _uiState.value.phone,
        )
        if (normalizedPhone.isBlank()) {
            showError("Ingresa un teléfono válido antes de reenviar el código.")
            return
        }

        sendLoginOtp(normalizedPhone, isResend = true)
    }

    fun resendRegisterOtp() {
        val normalizedPhone = buildInternationalPhone(
            countryCode = _uiState.value.phoneCountryCode,
            nationalNumber = _uiState.value.pacienteForm.telefono,
        )
        if (normalizedPhone.isBlank()) {
            showError("Ingresa un teléfono válido antes de reenviar el código.")
            return
        }

        viewModelScope.launch {
            setLoading(true)
            runCatching {
                sendRegisterOtp(normalizedPhone)
                RegistrationResult.OtpSent(
                    phone = normalizedPhone,
                    isResend = true,
                )
            }.onSuccess {
                updateRegisterOtpState(it)
            }.onFailure {
                showError("No se pudo reenviar el código: ${it.localizedMessage}")
            }
        }
    }

    private fun sendLoginOtp(
        normalizedPhone: String,
        isResend: Boolean,
    ) {
        viewModelScope.launch {
            setLoading(true)
            runCatching {
                val eligibility = pacienteRepository.getAuthEligibilityByTelefono(normalizedPhone)
                if (eligibility == null) {
                    navigateToRegister(normalizedPhone)
                    return@launch
                }

                if (!eligibility.authorized) {
                    if (eligibility.reason == "PATIENT_NOT_FOUND") {
                        navigateToRegister(normalizedPhone)
                        return@launch
                    }

                    error(
                        when (eligibility.reason) {
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
                val (countryCode, nationalNumber) = splitInternationalPhone(normalizedPhone)
                _uiState.update {
                    it.copy(
                        phoneCountryCode = countryCode,
                        phone = nationalNumber,
                        pacienteForm = it.pacienteForm.copy(telefono = nationalNumber),
                        otpSent = true,
                        infoMessage = if (isResend) {
                            "Te reenviamos el código por Whatsapp."
                        } else {
                            "Te enviamos un codigo por Whatsapp."
                        },
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
        val normalizedPhone = buildInternationalPhone(
            countryCode = _uiState.value.phoneCountryCode,
            nationalNumber = _uiState.value.phone,
        )
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
                val userId = authRepository.verifyOtp(normalizedPhone, otpCode)
                val eligibility = pacienteRepository
                    .getAuthEligibilityByTelefono(normalizedPhone)
                    ?: error("No se pudo obtener la elegibilidad")

                val paciente = eligibility.paciente
                    ?: error("No se pudo obtener el paciente")

                val pacienteId = paciente.id_paciente
                    ?: error("El paciente no tiene id_paciente")

                sessionManager.saveSession(
                    token = authRepository.currentAccessToken()
                        ?: error("No token"),
                    refreshToken = authRepository.currentRefreshToken(),
                    pacienteId = pacienteId
                )
                syncDeviceToken()



            }.onSuccess {
                val consentAccepted = legalRepository.hasAcceptedLatest()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        infoMessage = null,
                        errorMessage = null,
                        navigationTarget = if (consentAccepted) {
                            AuthNavigationTarget.SCHEDULE
                        } else {
                            AuthNavigationTarget.CONSENT
                        },
                        pacienteForm = it.pacienteForm.copy(telefono = it.phone),
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
        val normalizedPhone = buildInternationalPhone(
            countryCode = _uiState.value.phoneCountryCode,
            nationalNumber = form.telefono,
        )

        val validationError = validatePacienteForm(form, normalizedPhone)
        if (validationError != null) {
            showError(validationError)
            return
        }

        viewModelScope.launch {
            setLoading(true)
            runCatching {
                if (!_uiState.value.otpSent) {
                    sendRegisterOtp(normalizedPhone)
                    RegistrationResult.OtpSent(
                        phone = normalizedPhone,
                        isResend = false,
                    )
                } else {
                    val otpCode = _uiState.value.otpCode.trim()
                    if (otpCode.length < 4) {
                        error("Ingresa el código OTP completo.")
                    }

                    val userId = authRepository.verifyOtp(normalizedPhone, otpCode)
                    persistAuthenticatedSession()
                    val pacienteResponse = pacienteRepository.createPaciente(
                        CreatePacienteRequest(
                            tipoDocumento = form.tipoDocumento,
                            numeroDocumento = form.numeroDocumento.trim(),
                            nombres = form.nombres.trim(),
                            apellidos = form.apellidos.trim(),
                            fechaNacimiento = normalizeDate(form.fechaNacimiento),
                            telefono = normalizedPhone,
                            correo = form.correo.trim().ifBlank { null },
                            estado = "ACTIVO",
                            idEps = form.idEps.trim().toLong(),
                        )
                    )

                    sessionManager.saveSession(
                        token = authRepository.currentAccessToken()!!,
                        refreshToken = authRepository.currentRefreshToken(),
                        pacienteId = pacienteResponse.idPaciente
                    )
                    syncDeviceToken()

                    RegistrationResult.PacienteCreated
                }
            }.onSuccess {
                when (it) {
                    is RegistrationResult.OtpSent -> updateRegisterOtpState(it)
                    RegistrationResult.PacienteCreated -> {


                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                errorMessage = null,
                                infoMessage = "Paciente creado correctamente.",
                                navigationTarget = AuthNavigationTarget.CONSENT,
                            )
                        }
                    }
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

    private fun normalizeDate(input: String): String {
        return try {
            val parts = input.split("/")

            if (parts.size == 3) {
                val day = parts[0].padStart(2, '0')
                val month = parts[1].padStart(2, '0')
                val year = parts[2]

                "$year-$month-$day"
            } else {
                input
            }
        } catch (e: Exception) {
            input
        }
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
            form.idEps.isBlank() -> "Selecciona una EPS."
            form.idEps.toLongOrNull() == null -> "La EPS seleccionada no es válida."
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

    private fun navigateToRegister(phone: String) {
        val (countryCode, nationalNumber) = splitInternationalPhone(phone)
        _uiState.update {
            it.copy(
                phoneCountryCode = countryCode,
                phone = nationalNumber,
                pacienteForm = it.pacienteForm.copy(telefono = nationalNumber),
                isLoading = false,
                otpSent = false,
                otpCode = "",
                errorMessage = null,
                infoMessage = "No encontramos este teléfono. Completa el registro para continuar.",
                navigationTarget = AuthNavigationTarget.REGISTER,
            )
        }
    }

    private fun buildInternationalPhone(
        countryCode: String,
        nationalNumber: String,
    ): String {
        val digitsOnly = nationalNumber.filter(Char::isDigit)
        if (digitsOnly.isBlank()) {
            return ""
        }

        return "${CountryDialCodes.sanitizeDialCode(countryCode)}$digitsOnly"
    }

    private fun splitInternationalPhone(phone: String): Pair<String, String> {
        return CountryDialCodes.matchByInternationalPhone(phone)
    }

    private fun syncDeviceToken() {
        viewModelScope.launch {
            try {
                val token = tokenStorage.getToken().firstOrNull() ?: return@launch
                runCatching {
                    deviceRepository.sendFcmToken(token)
                }.onFailure { e ->
                    Log.w(TAG, "Failed to sync device token: ${e.message}", e)
                    // Do not rethrow - syncing device token must not crash the app
                }
            } catch (e: Exception) {
                Log.w(TAG, "Failed to obtain device token: ${e.message}", e)
            }
        }
    }

    private fun updateRegisterOtpState(result: RegistrationResult.OtpSent) {
        val (countryCode, nationalNumber) = splitInternationalPhone(result.phone)
        _uiState.update { state ->
            state.copy(
                phoneCountryCode = countryCode,
                phone = nationalNumber,
                pacienteForm = state.pacienteForm.copy(telefono = nationalNumber),
                otpSent = true,
                isLoading = false,
                errorMessage = null,
                infoMessage = if (result.isResend) {
                    "Te reenviamos el código por WhatsApp. Verifícalo para terminar el registro."
                } else {
                    "Te enviamos un código por WhatsApp. Verifícalo para terminar el registro."
                },
            )
        }
    }

    private suspend fun sendRegisterOtp(normalizedPhone: String) {
        authRepository.requestOtp(normalizedPhone, createUser = true)
    }

    private suspend fun persistAuthenticatedSession() {
        sessionManager.saveSession(
            token = authRepository.currentAccessToken()!!,
            refreshToken = authRepository.currentRefreshToken(),
            pacienteId = null
        )
    }

    private sealed interface RegistrationResult {
        data class OtpSent(
            val phone: String,
            val isResend: Boolean,
        ) : RegistrationResult
        data object PacienteCreated : RegistrationResult
    }

    fun resetPacienteForm() {
        _uiState.update { it.copy(pacienteForm = PacienteFormState()) }
    }
}