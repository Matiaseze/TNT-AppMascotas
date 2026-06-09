package com.appmascotasv2.smartpaws.presentation.feature.mascota

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.appmascotasv2.smartpaws.domain.model.mascota.Mascota
import com.appmascotasv2.smartpaws.domain.usecase.mascota.DeleteMascotaUseCase
import com.appmascotasv2.smartpaws.domain.usecase.mascota.GetMascotasUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MascotaViewModel(
    private val getMascotasUseCase: GetMascotasUseCase,
    private val deleteMascotaUseCase: DeleteMascotaUseCase,
    val userId: Int
) : ViewModel() {

    val mascotas: StateFlow<List<Mascota>> = getMascotasUseCase(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun deleteMascota(mascota: Mascota) {
        viewModelScope.launch { deleteMascotaUseCase(mascota) }
    }
}

class MascotaViewModelFactory(
    private val getMascotasUseCase: GetMascotasUseCase,
    private val deleteMascotaUseCase: DeleteMascotaUseCase,
    private val userId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MascotaViewModel(getMascotasUseCase, deleteMascotaUseCase, userId) as T
    }
}