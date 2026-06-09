package com.appmascotasv2.smartpaws.domain.model.mascota

data class Mascota(
    val id: Int = 0,
    val ownerId: Int,
    val nombre: String,
    val especie: String,
    val raza: String,
    val fechaCumpleanios: String,
    val peso: Double
)