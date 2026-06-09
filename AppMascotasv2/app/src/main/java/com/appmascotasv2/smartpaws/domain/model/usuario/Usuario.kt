package com.appmascotasv2.smartpaws.domain.model.usuario

data class Usuario(
    val id: Int = 0,
    val username: String,
    val provider: String = "local"  // "local" | "google" hacer este cambio cuando tengamos Firebase
)

// NOTA: El passwordHash no va en el modelo de dominio ya que es un detalle de implementacion de data.