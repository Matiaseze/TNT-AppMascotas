package com.example.appmascotasv1

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class AcercaDeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acerca_de)
        val iconPaw = findViewById<ImageView>(R.id.iconPaw)
        iconPaw.clipToOutline = true

    }
}