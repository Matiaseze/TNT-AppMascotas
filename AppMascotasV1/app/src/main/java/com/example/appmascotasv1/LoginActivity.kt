package com.example.appmascotasv1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.*
import android.content.Intent

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val user = findViewById<EditText>(R.id.loginUserOrMail)
        val pass = findViewById<EditText>(R.id.loginPassword)
        val btnLogin = findViewById<Button>(R.id.loginButton)

        btnLogin.setOnClickListener {
            ingresar(usuario = user, contrasenia = pass)
        }
    }

    private fun ingresar(usuario: EditText, contrasenia: EditText) {
        val vUsuario = usuario.text.toString().trim()
        val vContrasenia = contrasenia.text.toString().trim()

        if (vUsuario == "admin" && vContrasenia == "123456") {
            val intent = Intent(this@LoginActivity, MainMenuActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Bienvenido $vUsuario", Toast.LENGTH_SHORT).show()
        }
    }
}
