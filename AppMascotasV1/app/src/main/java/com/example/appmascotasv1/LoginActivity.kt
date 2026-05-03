package com.example.appmascotasv1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.*
import android.content.Intent
import android.content.SharedPreferences

class LoginActivity : AppCompatActivity() {


    private lateinit var prefs: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        val iconPaw = findViewById<ImageView>(R.id.iconPaw)
        iconPaw.clipToOutline = true
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        prefs = getSharedPreferences("login_prefs", MODE_PRIVATE)

        val user = findViewById<EditText>(R.id.loginUserOrMail)
        val pass = findViewById<EditText>(R.id.loginPassword)
        val btnLogin = findViewById<Button>(R.id.loginButton)
        val btnExit   = findViewById<Button>(R.id.exitButton)
        val remember  = findViewById<CheckBox>(R.id.checkBox)

        val savedUser     = prefs.getString("usuario", "")
        val savedRemember = prefs.getBoolean("recordar", false)
        // boton de recordarme
        if (savedRemember && !savedUser.isNullOrEmpty()) {
            user.setText(savedUser)
            remember.isChecked = true
        }
        // boton de ingresar
        btnLogin.setOnClickListener {
            ingresar(usuario = user, contrasenia = pass, remember = remember)
        }
        // boton de salir
        btnExit.setOnClickListener {
            finishAffinity() // Cierra todas las actividades y sale de la aplicación
        }
    }

    private fun ingresar(usuario: EditText, contrasenia: EditText, remember: CheckBox) {
        val vUsuario     = usuario.text.toString().trim()
        val vContrasenia = contrasenia.text.toString().trim()

        if (vUsuario == "admin" && vContrasenia == "123456") {

            // checkbox = true, guarda datos
            prefs.edit().apply {
                if (remember.isChecked) {
                    putString("usuario", vUsuario)
                    putBoolean("recordar", true)
                } else {
                    // checkbox = false, limpiar datos guardados
                    remove("usuario")
                    putBoolean("recordar", false)
                }
                apply()
            }

            Toast.makeText(this, "Bienvenido $vUsuario", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@LoginActivity, MainMenuActivity::class.java))

        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }
}
