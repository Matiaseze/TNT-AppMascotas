package com.appmascotasv2.smartpaws.presentation.feature.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.appmascotasv2.smartpaws.domain.model.usuario.Usuario
import com.appmascotasv2.smartpaws.domain.usecase.auth.GetUsuarioByIdUseCase
import com.appmascotasv2.smartpaws.domain.usecase.mascota.GetMascotasUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


data class PerfilUiState(
    val usuario: Usuario? = null,
    val cantidadMascotas: Int = 0,
    val isLoading: Boolean = true
)

class PerfilViewModel(
    private val getUsuarioByIdUseCase: GetUsuarioByIdUseCase,
    private val getMascotasUseCase: GetMascotasUseCase,
    private val userId: Int
) : ViewModel() {
    private val _usuario = MutableStateFlow<Usuario?>(null)

    val uiState: StateFlow<PerfilUiState> = combine(
        _usuario,
        getMascotasUseCase(userId)
    ) { usuario, mascotas ->
        PerfilUiState(
            usuario = usuario,
            cantidadMascotas = mascotas.size,
            isLoading = usuario == null
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PerfilUiState())

    init {
        viewModelScope.launch {
            _usuario.value = getUsuarioByIdUseCase(userId)
        }
    }
}

class PerfilViewModelFactory(
    private val getUsuarioByIdUseCase: GetUsuarioByIdUseCase,
    private val getMascotasUseCase: GetMascotasUseCase,
    private val userId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return PerfilViewModel(getUsuarioByIdUseCase, getMascotasUseCase, userId) as T
    }
}

