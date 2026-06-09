package com.appmascotasv2.smartpaws.feature.mascota

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items                  // ← faltaba este
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.appmascotasv2.smartpaws.R
import com.appmascotasv2.smartpaws.data.local.entity.MascotaEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotaScreen(
    viewModel: MascotaViewModel,
    onNavigateToRegisterPet: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val pets by viewModel.pets.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Mis Mascotas",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            "${pets.size} registradas",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter            = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick        = onNavigateToRegisterPet,
                icon           = {
                    Icon(
                        painter            = painterResource(R.drawable.ic_add),
                        contentDescription = null
                    )
                },
                text           = { Text("Nueva mascota") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor   = MaterialTheme.colorScheme.onPrimary
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        if (pets.isEmpty()) {
            Box(
                modifier         = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter            = painterResource(R.drawable.ic_pets),
                        contentDescription = null,
                        modifier           = Modifier.size(72.dp),
                        tint               = MaterialTheme.colorScheme.primaryContainer
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Aún no hay mascotas",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Tocá + para agregar la primera",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    start  = 16.dp,
                    end    = 16.dp,
                    top    = padding.calculateTopPadding() + 8.dp,
                    bottom = padding.calculateBottomPadding() + 96.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pets, key = { it.id }) { mascota ->
                    AnimatedVisibility(
                        visible = true,
                        enter   = fadeIn() + slideInVertically { it / 2 }
                    ) {
                        MascotaCard(
                            mascota  = mascota,
                            onDelete = { viewModel.deletePet(mascota) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MascotaCard(mascota: MascotaEntity, onDelete: () -> Unit) {
    var showDelete by remember { mutableStateOf(false) }

    if (showDelete) {
        AlertDialog(
            onDismissRequest = { showDelete = false },
            title            = { Text("Eliminar mascota") },
            text             = { Text("¿Eliminar a ${mascota.nombre}?") },
            confirmButton    = {
                TextButton(onClick = { showDelete = false; onDelete() }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDelete = false }) { Text("Cancelar") }
            }
        )
    }

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
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
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier              = Modifier.weight(1f)
            ) {
                Box(
                    modifier         = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter            = painterResource(R.drawable.ic_pets),
                        contentDescription = null,
                        tint               = MaterialTheme.colorScheme.primary,
                        modifier           = Modifier.size(26.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        mascota.nombre,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (mascota.especie.isNotBlank()) {
                        Text(
                            "${mascota.especie} • ${mascota.raza}",
                            style     = MaterialTheme.typography.bodyMedium,
                            color     = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontStyle = FontStyle.Italic
                        )
                    }
                    Text(
                        buildString {
                            if (mascota.peso > 0) append("${mascota.peso} kg")
                            if (mascota.peso > 0 && mascota.fechaCumpleanios.isNotBlank()) append("  ·  ")
                            if (mascota.fechaCumpleanios.isNotBlank()) append("Nac: ${mascota.fechaCumpleanios}")
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(onClick = { showDelete = true }) {
                Icon(
                    painter            = painterResource(R.drawable.ic_delete),
                    contentDescription = "Eliminar",
                    tint               = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}