package com.example.medix.presentation.viewmodels.auth


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medix.data.dto.UserProfileDto
import com.example.medix.domain.entities.Appointment
import com.example.medix.domain.repositories.LegalRepository
import com.example.medix.presentation.ui.state.UiState
import com.example.medix.presentation.viewmodels.status.ConsentUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ConsentViewModel @Inject constructor(
    private val repository: LegalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<ConsentUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<ConsentUiState>> = _uiState.asStateFlow()

    init {
        loadDocumento()
    }

    private fun loadDocumento() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                val doc = repository.getDocumento()

                _uiState.value = UiState.Success(
                    ConsentUiState(
                        document = doc
                    )
                )

            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error cargando documento")
            }
        }
    }

    fun onAccept() {
        val currentState = _uiState.value

        if (currentState is UiState.Success) {
            val doc = currentState.data.document ?: return

            viewModelScope.launch {
                try {
                    repository.aceptarDocumento(doc.id)

                    _uiState.value = UiState.Success(
                        currentState.data.copy(
                            accepted = true
                        )
                    )

                } catch (e: Exception) {
                    _uiState.value = UiState.Error("Error al aceptar")
                }
            }
        }
    }
}