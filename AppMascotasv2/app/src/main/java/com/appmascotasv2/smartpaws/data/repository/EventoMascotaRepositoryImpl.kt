package com.appmascotasv2.smartpaws.data.repository

import com.appmascotasv2.smartpaws.data.local.dao.EventoMascotaDao
import com.appmascotasv2.smartpaws.data.local.entity.EventoMascotaEntity
import com.appmascotasv2.smartpaws.domain.model.mascota.EventoMascota
import com.appmascotasv2.smartpaws.domain.repository.EventoMascotaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EventoMascotaRepositoryImpl(
    private val dao: EventoMascotaDao
) : EventoMascotaRepository {

    override fun getEventosByMascota(mascotaId: Int): Flow<List<EventoMascota>> {
        return dao.getEventosByMascota(mascotaId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllEventos(): Flow<List<EventoMascota>> {
        return dao.getAllEventos().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getEventosByPeriodo(start: Long, end: Long): Flow<List<EventoMascota>> {
        return dao.getEventosByPeriodo(start, end).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addEvento(evento: EventoMascota): Long {
        return dao.insert(evento.toEntity())
    }

    override suspend fun updateEvento(evento: EventoMascota) {
        dao.update(evento.toEntity())
    }

    override suspend fun deleteEvento(evento: EventoMascota) {
        dao.delete(evento.toEntity())
    }

    private fun EventoMascotaEntity.toDomain() = EventoMascota(
        id = id,
        mascotaId = mascotaId,
        fecha = fecha,
        titulo = titulo,
        descripcion = descripcion,
        tipo = tipo
    )

    private fun EventoMascota.toEntity() = EventoMascotaEntity(
        id = id,
        mascotaId = mascotaId,
        fecha = fecha,
        titulo = titulo,
        descripcion = descripcion,
        tipo = tipo
    )
}