package com.appmascotasv2.smartpaws.feature.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.appmascotasv2.smartpaws.R
import com.appmascotasv2.smartpaws.feature.about.AboutScreen
import com.appmascotasv2.smartpaws.feature.perfil.PerfilScreen

// Tabs de la bottom bar
enum class MainTab { HOME, CALENDARIO, PERFIL }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    userId: Int,
    onNavigateToMascotas: () -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(MainTab.HOME) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor   = MaterialTheme.colorScheme.onPrimary
            ) {
                NavigationBarItem(
                    selected = selectedTab == MainTab.HOME,
                    onClick  = { selectedTab = MainTab.HOME },
                    icon     = {
                        Icon(
                            painter            = painterResource(R.drawable.ic_home),
                            contentDescription = "Inicio"
                        )
                    },
                    label  = { Text("Inicio") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor   = MaterialTheme.colorScheme.onPrimary,
                        selectedTextColor   = MaterialTheme.colorScheme.onPrimary,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                        unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                        indicatorColor      = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == MainTab.CALENDARIO,
                    onClick  = { selectedTab = MainTab.CALENDARIO },
                    icon     = {
                        Icon(
                            painter            = painterResource(R.drawable.ic_date_range),
                            contentDescription = "Calendario"
                        )
                    },
                    label  = { Text("Calendario") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor   = MaterialTheme.colorScheme.onPrimary,
                        selectedTextColor   = MaterialTheme.colorScheme.onPrimary,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                        unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                        indicatorColor      = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == MainTab.PERFIL,
                    onClick  = { selectedTab = MainTab.PERFIL },
                    icon     = {
                        Icon(
                            painter            = painterResource(R.drawable.ic_person),
                            contentDescription = "Perfil"
                        )
                    },
                    label  = { Text("Perfil") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor   = MaterialTheme.colorScheme.onPrimary,
                        selectedTextColor   = MaterialTheme.colorScheme.onPrimary,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                        unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                        indicatorColor      = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Box(modifier = Modifier.padding(padding)) {
            when (selectedTab) {
                MainTab.HOME       -> HomeTab(onNavigateToMascotas = onNavigateToMascotas, onLogout = onLogout)
                MainTab.CALENDARIO -> CalendarioTab()
                MainTab.PERFIL     -> PerfilScreen(onNavigateBack = { selectedTab = MainTab.HOME })
            }
        }
    }
}

// ── Tab: Inicio ────────────────────────────────────────────────────────────────
@Composable
private fun HomeTab(
    onNavigateToMascotas: () -> Unit,
    onLogout: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title            = { Text("Cerrar sesión") },
            text             = { Text("¿Querés salir de tu cuenta?") },
            confirmButton    = {
                TextButton(onClick = { showLogoutDialog = false; onLogout() }) {
                    Text("Salir", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancelar") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        // Header
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text  = "SmartPaws",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text  = "¿Qué querés hacer hoy?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = { showLogoutDialog = true }) {
                Icon(
                    painter            = painterResource(R.drawable.ic_logout),
                    contentDescription = "Cerrar sesión",
                    tint               = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        // Grid de opciones
        val items = listOf(
            Triple("Mis Mascotas", "Ver y registrar", R.drawable.ic_pets),
            Triple("Acerca De",    "Info de la app",  R.drawable.ic_about)
        )

        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Card Mis Mascotas
            MenuCard(
                title    = "Mis Mascotas",
                subtitle = "Ver y registrar",
                iconRes  = R.drawable.ic_pets,
                onClick  = onNavigateToMascotas,
                modifier = Modifier.weight(1f)
            )
            // Card Acerca De
            MenuCard(
                title    = "Acerca De",
                subtitle = "Info de la app",
                iconRes  = R.drawable.ic_about,
                onClick  = { /* AboutScreen se puede abrir como dialog o nav */ },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// ── Tab: Calendario ────────────────────────────────────────────────────────────
@Composable
private fun CalendarioTab() {
    Box(
        modifier         = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter            = painterResource(R.drawable.ic_date_range),
                contentDescription = null,
                modifier           = Modifier.size(64.dp),
                tint               = MaterialTheme.colorScheme.primaryContainer
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text  = "Calendario",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text  = "Próximamente...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ── Componente Card del menú ───────────────────────────────────────────────────
@Composable
private fun MenuCard(
    title: String,
    subtitle: String,
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier
            .aspectRatio(1f)
            .clickable { onClick() },
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier              = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement   = Arrangement.Center,
            horizontalAlignment   = Alignment.CenterHorizontally
        ) {
            Box(
                modifier         = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter            = painterResource(iconRes),
                    contentDescription = null,
                    tint               = MaterialTheme.colorScheme.onPrimary,
                    modifier           = Modifier.size(30.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text      = title,
                style     = MaterialTheme.typography.titleLarge,
                color     = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text      = subtitle,
                style     = MaterialTheme.typography.bodyMedium,
                color     = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}