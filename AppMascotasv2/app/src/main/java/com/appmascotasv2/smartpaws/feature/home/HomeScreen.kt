package com.appmascotasv2.smartpaws.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.appmascotasv2.smartpaws.R
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextAlign

data class MenuItem(
    val title: String,
    val subtitle: String,
    val iconRes: Int,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToPets: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onLogout: () -> Unit
) {
    val menuItems = listOf(
        MenuItem(
            title    = "Mis Mascotas",
            subtitle = "Ver y registrar mascotas",
            iconRes  = R.drawable.ic_pets,
            onClick  = onNavigateToPets
        ),
        MenuItem(
            title    = "Mi Perfil",
            subtitle = "Datos de tu cuenta",
            iconRes  = R.drawable.ic_person,
            onClick  = onNavigateToProfile
        ),
        MenuItem(
            title    = "Acerca De",
            subtitle = "Información de la app",
            iconRes  = R.drawable.ic_about,
            onClick  = onNavigateToAbout
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text  = "SmartPaws",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text  = "¿Qué querés hacer hoy?",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            painter            = painterResource(R.drawable.ic_logout),
                            contentDescription = "Cerrar sesión"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Grid 2x2 — primera fila
            Row(
                modifier            = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MenuCard(item = menuItems[0], modifier = Modifier.weight(1f))
                MenuCard(item = menuItems[1], modifier = Modifier.weight(1f))
            }

            // Tercera card centrada
            Row(
                modifier            = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MenuCard(item = menuItems[2], modifier = Modifier.weight(1f))
                // Celda vacía para mantener el grid
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun MenuCard(item: MenuItem, modifier: Modifier = Modifier) {
    Card(
        modifier  = modifier
            .aspectRatio(1f)
            .clickable { item.onClick() },
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement   = Arrangement.Center,
            horizontalAlignment   = Alignment.CenterHorizontally
        ) {
            Box(
                modifier         = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter            = painterResource(item.iconRes),
                    contentDescription = null,
                    tint               = MaterialTheme.colorScheme.primary,
                    modifier           = Modifier.size(30.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text      = item.title,
                style     = MaterialTheme.typography.titleLarge,
                color     = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text      = item.subtitle,
                style     = MaterialTheme.typography.bodyMedium,
                color     = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}