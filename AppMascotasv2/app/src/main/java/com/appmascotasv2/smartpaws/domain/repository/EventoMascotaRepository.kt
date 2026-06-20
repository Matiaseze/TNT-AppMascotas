package com.appmascotasv2.smartpaws.domain.repository

import com.appmascotasv2.smartpaws.domain.model.mascota.EventoMascota
import kotlinx.coroutines.flow.Flow

interface EventoMascotaRepository {
    fun getEventosByMascota(mascotaId: Int): Flow<List<EventoMascota>>
    fun getAllEventos(): Flow<List<EventoMascota>>
    suspend fun addEvento(evento: EventoMascota)
    suspend fun updateEvento(evento: EventoMascota)
    suspend fun deleteEvento(evento: EventoMascota)
}