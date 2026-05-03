package com.example.appmascotasv1

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnMascotas = findViewById<LinearLayout>(R.id.btnMascotas)
        val btnAcerca = findViewById<LinearLayout>(R.id.btnAcerca)

        btnMascotas.setOnClickListener {
            startActivity(Intent(this, ListadoMascotasActivity::class.java))
        }

        btnAcerca.setOnClickListener {
            startActivity(Intent(this, AcercaDeActivity::class.java))
        }
    }
}