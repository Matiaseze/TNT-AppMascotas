package com.appmascotasv2.smartpaws.presentation.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.appmascotasv2.smartpaws.domain.model.mascota.EventoMascota
import com.appmascotasv2.smartpaws.domain.model.mascota.Mascota
import com.appmascotasv2.smartpaws.domain.repository.EventoMascotaRepository
import com.appmascotasv2.smartpaws.domain.repository.MascotaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CalendarioViewModel(
    private val eventoRepository: EventoMascotaRepository,
    private val mascotaRepository: MascotaRepository,
    private val userId: Int
) : ViewModel() {

    val mascotas: StateFlow<List<Mascota>> = mascotaRepository.getMascotasByOwner(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val eventos: StateFlow<List<EventoMascota>> = eventoRepository.getAllEventos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addEvento(evento: EventoMascota) {
        viewModelScope.launch {
            eventoRepository.addEvento(evento)
        }
    }

    fun deleteEvento(evento: EventoMascota) {
        viewModelScope.launch {
            eventoRepository.deleteEvento(evento)
        }
    }
}

class CalendarioViewModelFactory(
    private val eventoRepository: EventoMascotaRepository,
    private val mascotaRepository: MascotaRepository,
    private val userId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalendarioViewModel(eventoRepository, mascotaRepository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}