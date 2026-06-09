package com.appmascotasv2.smartpaws.presentation.feature.mascota

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.appmascotasv2.smartpaws.domain.model.mascota.Mascota
import com.appmascotasv2.smartpaws.domain.usecase.mascota.AddMascotaUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegistrarMascotaUiState(
    val isSaved:     Boolean = false,
    val nameError:   Boolean = false,
    val weightError: Boolean = false
)

class RegistrarMascotaViewModel(
    private val addMascotaUseCase: AddMascotaUseCase,
    private val userId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrarMascotaUiState())
    val uiState: StateFlow<RegistrarMascotaUiState> = _uiState.asStateFlow()

    fun register(
        nombre: String,
        especie: String,
        raza: String,
        fechaCumpleanios: String,
        peso: String
    ) {
        val nameErr   = nombre.isBlank()
        val weightErr = peso.isNotBlank() && peso.toDoubleOrNull() == null

        _uiState.value = _uiState.value.copy(
            nameError   = nameErr,
            weightError = weightErr
        )

        if (nameErr || weightErr) return

        viewModelScope.launch {
            addMascotaUseCase(
                Mascota(
                    ownerId          = userId,
                    nombre           = nombre.trim(),
                    especie          = especie.trim(),
                    raza             = raza.trim(),
                    fechaCumpleanios = fechaCumpleanios.trim(),
                    peso             = peso.toDoubleOrNull() ?: 0.0
                )
            )
            _uiState.value = _uiState.value.copy(isSaved = true)
        }
    }

    fun clearErrors() {
        _uiState.value = _uiState.value.copy(nameError = false, weightError = false)
    }
}

class RegistrarMascotaViewModelFactory(
    private val addMascotaUseCase: AddMascotaUseCase,
    private val userId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return RegistrarMascotaViewModel(addMascotaUseCase, userId) as T
    }
}