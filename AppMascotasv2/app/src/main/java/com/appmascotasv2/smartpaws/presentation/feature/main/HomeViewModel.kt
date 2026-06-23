package com.appmascotasv2.smartpaws.presentation.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.appmascotasv2.smartpaws.domain.model.mascota.EventoMascota
import com.appmascotasv2.smartpaws.domain.model.mascota.Mascota
import com.appmascotasv2.smartpaws.domain.model.mascota.TipoEvento
import com.appmascotasv2.smartpaws.domain.repository.EventoMascotaRepository
import com.appmascotasv2.smartpaws.domain.repository.MascotaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class MascotaConProximoTurno(
    val mascota: Mascota,
    val proximoTurno: EventoMascota?
)

data class HomeUiState(
    val proximoEvento: EventoMascota? = null,
    val proximoEventoMascotaNombre: String = "",
    val mascotasConTurno: List<MascotaConProximoTurno> = emptyList(),
    val proximosEventos: List<Pair<EventoMascota, String>> = emptyList(),
    val cantidadMascotas: Int = 0
)

class HomeViewModel(
    private val mascotaRepository: MascotaRepository,
    private val eventoRepository: EventoMascotaRepository,
    private val userId: Int
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        mascotaRepository.getMascotasByOwner(userId),
        eventoRepository.getAllEventos()
    ) { mascotas, eventos ->

        val ahora = System.currentTimeMillis()

        val mascotaIds = mascotas.map { it.id }.toSet()
        val turnosFuturos = eventos
            .filter { it.tipo == TipoEvento.TURNO_VETERINARIO }
            .filter { it.mascotaId in mascotaIds }
            .filter { it.fecha >= ahora }
            .sortedBy { it.fecha }

        // Todos los eventos futuros (cualquier tipo), ordenados por fecha
        val proximosEventos = eventos
            .filter { it.mascotaId in mascotaIds }
            .filter { it.fecha >= ahora }
            .sortedBy { it.fecha }
            .map { evento ->
                val nombreMascota = mascotas.find { it.id == evento.mascotaId }?.nombre ?: ""
                Pair(evento, nombreMascota)
            }

        val proximoEvento = turnosFuturos.firstOrNull()
        val proximoEventoMascotaNombre = proximoEvento?.let { ev ->
            mascotas.find { it.id == ev.mascotaId }?.nombre
        } ?: ""

        val mascotasConTurno = mascotas.map { mascota ->
            MascotaConProximoTurno(
                mascota      = mascota,
                proximoTurno = turnosFuturos.firstOrNull { it.mascotaId == mascota.id }
            )
        }

        HomeUiState(
            proximoEvento               = proximoEvento,
            proximoEventoMascotaNombre  = proximoEventoMascotaNombre,
            mascotasConTurno            = mascotasConTurno,
            proximosEventos             = proximosEventos,
            cantidadMascotas            = mascotas.size
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())
}

class HomeViewModelFactory(
    private val mascotaRepository: MascotaRepository,
    private val eventoRepository: EventoMascotaRepository,
    private val userId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HomeViewModel(mascotaRepository, eventoRepository, userId) as T
    }
}