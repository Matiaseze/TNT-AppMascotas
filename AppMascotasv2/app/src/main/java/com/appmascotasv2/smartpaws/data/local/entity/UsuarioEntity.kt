package com.appmascotasv2.smartpaws.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val passwordHash: String,   // SHA-256
    val provider: String = "local"  // "local" | "google" — para Firebase a futuro
)