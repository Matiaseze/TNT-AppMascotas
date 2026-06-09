package com.appmascotasv2.smartpaws.presentation.feature.mascota.model

object MascotaCatalog {

    val especies = listOf(
        "Perro",
        "Gato"
    )

    val razasPorEspecie = mapOf(
        "Perro" to listOf(
            "Mestizo",
            "Labrador Retriever",
            "Golden Retriever",
            "Pastor Alemán",
            "Caniche",
            "Bulldog Francés",
            "Beagle"
        ),
        "Gato" to listOf(
            "Mestizo",
            "Tabby",
            "Siamés",
            "Persa",
            "Maine Coon",
            "Bengalí"
        )
    )
}