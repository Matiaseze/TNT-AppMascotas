package com.appmascotasv2.smartpaws

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.appmascotasv2.smartpaws.app.navigation.NavGraph
import com.appmascotasv2.smartpaws.app.ui.theme.AppMascotasv2Theme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as AppMascotas).container

        // Leer sesión persistida antes de mostrar la UI
        lifecycleScope.launch {
            val userId = container.authRepository.loggedUserId.first()

            setContent {
                AppMascotasv2Theme() {
                    NavGraph(
                        container     = container,
                        startUserId   = userId
                    )
                }
            }
        }
    }
}