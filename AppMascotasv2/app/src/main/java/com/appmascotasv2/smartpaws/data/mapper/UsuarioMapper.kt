package com.appmascotasv2.smartpaws.data.mapper


import com.appmascotasv2.smartpaws.data.local.entity.UsuarioEntity
import com.appmascotasv2.smartpaws.domain.model.usuario.Usuario

fun UsuarioEntity.toDomain(): Usuario = Usuario(
    id       = id,
    username = username,
    provider = provider
)

// NOTA: No hay toEntity() para Usuario porque la creación de usuarios siempre
// pasa por el repositorio con el hash, la pantalla nunca construye una UsuarioEntity directamente.