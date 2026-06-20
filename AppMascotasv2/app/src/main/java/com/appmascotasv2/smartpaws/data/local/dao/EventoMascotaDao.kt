package com.appmascotasv2.smartpaws.data.local.dao

import androidx.room.*
import com.appmascotasv2.smartpaws.data.local.entity.EventoMascotaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventoMascotaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(evento: EventoMascotaEntity): Long

    @Update
    suspend fun update(evento: EventoMascotaEntity)

    @Delete
    suspend fun delete(evento: EventoMascotaEntity)

    @Query("SELECT * FROM eventos_mascota WHERE mascotaId = :mascotaId ORDER BY fecha ASC")
    fun getEventosByMascota(mascotaId: Int): Flow<List<EventoMascotaEntity>>

    @Query("SELECT * FROM eventos_mascota WHERE fecha BETWEEN :start AND :end ORDER BY fecha ASC")
    fun getEventosByPeriodo(start: Long, end: Long): Flow<List<EventoMascotaEntity>>

    @Query("SELECT * FROM eventos_mascota ORDER BY fecha ASC")
    fun getAllEventos(): Flow<List<EventoMascotaEntity>>
}