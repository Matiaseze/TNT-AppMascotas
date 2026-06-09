package com.appmascotasv2.smartpaws.domain.usecase.mascota

import com.appmascotasv2.smartpaws.domain.model.mascota.Mascota
import com.appmascotasv2.smartpaws.domain.repository.MascotaRepository

class DeleteMascotaUseCase(private val repository: MascotaRepository) {
    suspend operator fun invoke(mascota: Mascota) =
        repository.deleteMascota(mascota)
}