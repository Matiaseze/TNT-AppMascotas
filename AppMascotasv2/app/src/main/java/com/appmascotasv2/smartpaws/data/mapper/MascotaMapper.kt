package com.appmascotasv2.smartpaws.data.mapper

import com.appmascotasv2.smartpaws.data.local.entity.MascotaEntity
import com.appmascotasv2.smartpaws.domain.model.mascota.Mascota

fun MascotaEntity.toDomain(): Mascota = Mascota(
    id               = id,
    ownerId          = ownerId,
    nombre           = nombre,
    especie          = especie,
    raza             = raza,
    fechaCumpleanios = fechaCumpleanios,
    peso             = peso
)

fun Mascota.toEntity(): MascotaEntity = MascotaEntity(
    id               = id,
    ownerId          = ownerId,
    nombre           = nombre,
    especie          = especie,
    raza             = raza,
    fechaCumpleanios = fechaCumpleanios,
    peso             = peso
)