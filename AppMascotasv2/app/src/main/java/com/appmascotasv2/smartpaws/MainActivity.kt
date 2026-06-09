package com.appmascotasv2.smartpaws


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.appmascotasv2.smartpaws.presentation.navigation.NavGraph
import com.appmascotasv2.smartpaws.presentation.ui.theme.AppMascotasv2Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as AppMascotas).container

        setContent {
            AppMascotasv2Theme {

                val userId by container.getLoggedUserIdUseCase()
                    .collectAsState(initial = 0)

                NavGraph(
                    container   = container,
                    startUserId = userId
                )
            }
        }
    }
}