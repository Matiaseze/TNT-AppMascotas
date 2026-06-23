package com.appmascotasv2.smartpaws.presentation.feature.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.appmascotasv2.smartpaws.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    viewModel: PerfilViewModel,
    onNavigateToMascotas: () -> Unit,
    onNavigateToAbout: () -> Unit,       // ← nuevo parámetro
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
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
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text     = "Mi Perfil",
            style    = MaterialTheme.typography.headlineLarge,
            color    = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(28.dp))

        // ── Avatar ───────────────────────────────────────────────────────
        Box(
            modifier         = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter            = painterResource(R.drawable.ic_person),
                contentDescription = "Avatar",
                tint               = MaterialTheme.colorScheme.primary,
                modifier           = Modifier.size(48.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        // ── Username ─────────────────────────────────────────────────────
        Text(
            text  = uiState.usuario?.username ?: "...",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(32.dp))

        // ── Card "Mis Mascotas" ───────────────────────────────────────────
        PerfilCard(
            iconRes  = R.drawable.ic_pets,
            titulo   = "Mis Mascotas",
            subtitulo = "${uiState.cantidadMascotas} registradas",
            onClick  = onNavigateToMascotas
        )

        Spacer(Modifier.height(12.dp))

        // ── Card "Acerca De" ──────────────────────────────────────────────
        PerfilCard(
            iconRes   = R.drawable.ic_about,
            titulo    = "Acerca De",
            subtitulo = "Información de la app",
            onClick   = onNavigateToAbout
        )

        Spacer(Modifier.weight(1f))

        // ── Botón cerrar sesión ───────────────────────────────────────────
        OutlinedButton(
            onClick  = { showLogoutDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(
                painter            = painterResource(R.drawable.ic_logout),
                contentDescription = null,
                modifier           = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("Cerrar sesión", style = MaterialTheme.typography.titleLarge)
        }
    }
}

// ── Componente reutilizable para cards del perfil ─────────────────────────────
@Composable
private fun PerfilCard(
    iconRes: Int,
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit
) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier         = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter            = painterResource(iconRes),
                        contentDescription = null,
                        tint               = MaterialTheme.colorScheme.onPrimary,
                        modifier           = Modifier.size(24.dp)
                    )
                }
                Column {
                    Text(
                        titulo,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        subtitulo,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Icon(
                painter            = painterResource(R.drawable.ic_arrow_back),
                contentDescription = null,
                modifier           = Modifier
                    .size(20.dp)
                    .rotate(180f),
                tint               = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}