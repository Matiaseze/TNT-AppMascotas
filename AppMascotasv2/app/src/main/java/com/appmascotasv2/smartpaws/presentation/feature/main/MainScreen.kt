package com.appmascotasv2.smartpaws.presentation.feature.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appmascotasv2.smartpaws.R
import com.appmascotasv2.smartpaws.di.AppContainer
import com.appmascotasv2.smartpaws.domain.model.mascota.EventoMascota
import com.appmascotasv2.smartpaws.domain.model.mascota.Mascota
import com.appmascotasv2.smartpaws.domain.model.mascota.TipoEvento
import com.appmascotasv2.smartpaws.presentation.feature.perfil.PerfilScreen
import java.util.*

enum class MainTab { HOME, CALENDARIO, PERFIL }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    userId: Int,
    container: AppContainer,
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
                    icon     = { Icon(painterResource(R.drawable.ic_home), "Inicio") },
                    label    = { Text("Inicio") },
                    colors   = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        indicatorColor    = MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                        unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                        selectedTextColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == MainTab.CALENDARIO,
                    onClick  = { selectedTab = MainTab.CALENDARIO },
                    icon     = { Icon(painterResource(R.drawable.ic_date_range), "Calendario") },
                    label    = { Text("Calendario") },
                    colors   = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        indicatorColor    = MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                        unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                        selectedTextColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == MainTab.PERFIL,
                    onClick  = { selectedTab = MainTab.PERFIL },
                    icon     = { Icon(painterResource(R.drawable.ic_person), "Perfil") },
                    label    = { Text("Perfil") },
                    colors   = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        indicatorColor    = MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                        unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                        selectedTextColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedTab) {
                MainTab.HOME       -> HomeTab(onNavigateToMascotas, onLogout)
                MainTab.CALENDARIO -> {
                    val vm: CalendarioViewModel = viewModel(
                        factory = CalendarioViewModelFactory(
                            container.eventoMascotaRepository,
                            container.mascotaRepository,
                            userId
                        )
                    )
                    CalendarioTab(vm)
                }
                MainTab.PERFIL     -> PerfilScreen(onNavigateBack = { selectedTab = MainTab.HOME })
            }
        }
    }
}

@Composable
private fun HomeTab(onNavigateToMascotas: () -> Unit, onLogout: () -> Unit) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Querés salir de tu cuenta?") },
            confirmButton = {
                TextButton(onClick = { showLogoutDialog = false; onLogout() }) {
                    Text("Salir", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancelar") }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("SmartPaws", style = MaterialTheme.typography.headlineLarge)
                Text("¿Qué querés hacer hoy?", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = { showLogoutDialog = true }) {
                Icon(painterResource(R.drawable.ic_logout), "Cerrar sesión")
            }
        }

        Spacer(Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MenuCard("Mis Mascotas", "Ver y registrar", R.drawable.ic_pets, onNavigateToMascotas, Modifier.weight(1f))
            MenuCard("Acerca De", "Info de la app", R.drawable.ic_about, {}, Modifier.weight(1f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalendarioTab(viewModel: CalendarioViewModel) {
    val eventos by viewModel.eventos.collectAsState()
    val mascotas by viewModel.mascotas.collectAsState()
    val datePickerState = rememberDatePickerState()
    var showAddDialog by remember { mutableStateOf(false) }
    val selectedDate = datePickerState.selectedDateMillis ?: System.currentTimeMillis()

    Column(modifier = Modifier.fillMaxSize()) {
        DatePicker(state = datePickerState, title = null, headline = null, showModeToggle = false)
        HorizontalDivider()
        
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Eventos del día", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.size(40.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(painterResource(R.drawable.ic_add), "Agregar", tint = Color.White)
            }
        }

        val eventosDelDia = eventos.filter {
            val c1 = Calendar.getInstance().apply { timeInMillis = it.fecha }
            val c2 = Calendar.getInstance().apply { timeInMillis = selectedDate }
            c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
        }

        if (eventosDelDia.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay registros.", color = Color.Gray)
            }
        } else {
            LazyColumn(Modifier.padding(horizontal = 16.dp)) {
                items(eventosDelDia) { ev: EventoMascota ->
                    EventItem(ev, mascotas.find { it.id == ev.mascotaId }?.nombre ?: "Mascota")
                }
            }
        }
    }

    if (showAddDialog) {
        if (mascotas.isEmpty()) {
            AlertDialog(onDismissRequest = { showAddDialog = false }, 
                title = { Text("Sin mascotas") },
                text = { Text("Registra una mascota primero.") },
                confirmButton = { TextButton(onClick = { showAddDialog = false }) { Text("OK") } }
            )
        } else {
            AddEventoDialog(mascotas, { showAddDialog = false }) { t, d, mId, tipo ->
                viewModel.addEvento(EventoMascota(mascotaId = mId, fecha = selectedDate, titulo = t, descripcion = d, tipo = tipo))
                showAddDialog = false
            }
        }
    }
}

@Composable
fun EventItem(evento: EventoMascota, mascotaNombre: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val icon = if (evento.tipo == TipoEvento.TURNO_VETERINARIO) R.drawable.ic_date_range else R.drawable.ic_pets
                Icon(painterResource(icon), null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text(evento.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Text("Mascota: $mascotaNombre", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            if (evento.descripcion.isNotBlank()) Text(evento.descripcion, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventoDialog(
    mascotas: List<Mascota>,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int, TipoEvento) -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var selectedMascota by remember { mutableStateOf(mascotas.first()) }
    var tipo by remember { mutableStateOf(TipoEvento.REGISTRO) }
    var expM by remember { mutableStateOf(false) }
    var expT by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Evento") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
                
                ExposedDropdownMenuBox(expanded = expM, onExpandedChange = { expM = !expM }) {
                    OutlinedTextField(
                        value = selectedMascota.nombre, onValueChange = {}, readOnly = true, label = { Text("Mascota") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expM) }, modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expM, onDismissRequest = { expM = false }) {
                        mascotas.forEach { m -> DropdownMenuItem(text = { Text(m.nombre) }, onClick = { selectedMascota = m; expM = false }) }
                    }
                }

                ExposedDropdownMenuBox(expanded = expT, onExpandedChange = { expT = !expT }) {
                    OutlinedTextField(
                        value = if (tipo == TipoEvento.REGISTRO) "Registro" else "Turno Vet", onValueChange = {}, readOnly = true, label = { Text("Tipo") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expT) }, modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expT, onDismissRequest = { expT = false }) {
                        TipoEvento.values().forEach { t ->
                            DropdownMenuItem(text = { Text(if (t == TipoEvento.REGISTRO) "Registro" else "Turno Vet") }, onClick = { tipo = t; expT = false })
                        }
                    }
                }
            }
        },
        confirmButton = { Button(onClick = { onConfirm(titulo, desc, selectedMascota.id, tipo) }, enabled = titulo.isNotBlank()) { Text("Guardar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
private fun MenuCard(title: String, subtitle: String, iconRes: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.aspectRatio(1f).clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(56.dp).clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center) {
                Icon(painterResource(iconRes), null, tint = Color.White, modifier = Modifier.size(30.dp))
            }
            Spacer(Modifier.height(12.dp))
            Text(title, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        }
    }
}