package com.appmascotasv2.smartpaws.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "mascotas",
    foreignKeys = [ForeignKey(
        entity        = UsuarioEntity::class,
        parentColumns = ["id"],
        childColumns  = ["ownerId"],
        onDelete      = ForeignKey.CASCADE
    )]
)
data class MascotaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ownerId: Int,           // FK ---->>>> users.id
    val nombre: String,
    val especie: String,
    val raza: String,
    val fechaCumpleanios: String,
    val peso: Double
)