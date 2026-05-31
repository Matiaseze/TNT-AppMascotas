package com.appmascotasv2.smartpaws.data.local.dao

import androidx.room.*
import com.appmascotasv2.smartpaws.data.local.entity.MascotaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MascotaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mascota: MascotaEntity): Long

    @Update
    suspend fun update(mascota: MascotaEntity)

    @Delete
    suspend fun delete(mascota: MascotaEntity)

    @Query("SELECT * FROM mascotas WHERE ownerId = :ownerId ORDER BY nombre ASC")
    fun getMascotasByOwner(ownerId: Int): Flow<List<MascotaEntity>>
}