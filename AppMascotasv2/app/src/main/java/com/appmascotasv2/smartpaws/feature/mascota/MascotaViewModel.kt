package com.appmascotasv2.smartpaws.feature.mascota

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.appmascotasv2.smartpaws.data.local.entity.MascotaEntity
import com.appmascotasv2.smartpaws.data.repository.MascotaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MascotaViewModel(
    private val mascotaRepository: MascotaRepository,
    val userId: Int
) : ViewModel() {

    val pets: StateFlow<List<MascotaEntity>> = mascotaRepository
        .getPetsForUser(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun deletePet(pet: MascotaEntity) {
        viewModelScope.launch { mascotaRepository.deleteMascota(pet) }
    }
}

class MascotaViewModelFactory(
    private val mascotaRepository: MascotaRepository,
    private val userId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MascotaViewModel(mascotaRepository, userId) as T
    }
}