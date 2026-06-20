package com.appmascotasv2.smartpaws.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.appmascotasv2.smartpaws.domain.model.mascota.TipoEvento

@Entity(
    tableName = "eventos_mascota",
    foreignKeys = [ForeignKey(
        entity = MascotaEntity::class,
        parentColumns = ["id"],
        childColumns = ["mascotaId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class EventoMascotaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val mascotaId: Int,
    val fecha: Long,
    val titulo: String,
    val descripcion: String,
    val tipo: TipoEvento
)