package com.appmascotasv2.smartpaws.domain.model.mascota

data class EventoMascota(
    val id: Int = 0,
    val mascotaId: Int,
    val fecha: Long, // Timestamp
    val titulo: String,
    val descripcion: String,
    val tipo: TipoEvento = TipoEvento.REGISTRO
)

enum class TipoEvento {
    REGISTRO,
    TURNO_VETERINARIO
}