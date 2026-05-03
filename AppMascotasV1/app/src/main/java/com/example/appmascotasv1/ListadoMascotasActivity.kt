package com.example.appmascotasv1

import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ListadoMascotasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_mascotas)
        val iconPaw = findViewById<ImageView>(R.id.iconPaw)
        iconPaw.clipToOutline = true

        val listView = findViewById<ListView>(R.id.listMascotas)

        val mascotas = listOf(
            Mascota("Firulais", "Perro", "3 años"),
            Mascota("Mishi", "Gato", "2 años")
        )

        val adapter = MascotaAdapter(this, mascotas)
        listView.adapter = adapter

    }
}