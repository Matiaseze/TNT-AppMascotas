package com.appmascotasv2.smartpaws.presentation.feature.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.appmascotasv2.smartpaws.presentation.feature.perfil.PerfilViewModel
import com.appmascotasv2.smartpaws.presentation.feature.perfil.PerfilViewModelFactory
import java.text.SimpleDateFormat
import java.util.*


enum class MainTab { HOME, CALENDARIO, PERFIL }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    userId: Int,
    container: AppContainer,
    onNavigateToMascotas: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by rememberSaveable { mutableStateOf(MainTab.HOME) }

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
                MainTab.HOME       -> {
                    val vm: HomeViewModel = viewModel(
                        factory = HomeViewModelFactory(
                            mascotaRepository = container.mascotaRepository,
                            eventoRepository  = container.eventoMascotaRepository,
                            userId            = userId
                        )
                    )
                    HomeTab(viewModel = vm, onNavigateToMascotas = onNavigateToMascotas,
                        onNavigateToRegistrar = onNavigateToMascotas)
                }
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
                MainTab.PERFIL -> {
                    val vm: PerfilViewModel = viewModel(
                        factory = PerfilViewModelFactory(
                            getUsuarioByIdUseCase = container.getUsuarioByIdUseCase,
                            getMascotasUseCase    = container.getMascotasUseCase,
                            userId                = userId
                        )
                    )
                    PerfilScreen(
                        viewModel             = vm,
                        onNavigateToMascotas  = onNavigateToMascotas,
                        onNavigateToAbout     = onNavigateToAbout,
                        onLogout              = onLogout
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeTab(
    viewModel: HomeViewModel,
    onNavigateToMascotas: () -> Unit,
    onNavigateToRegistrar: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val formatterCompleto = remember { SimpleDateFormat("dd/MM/yyyy 'a las' HH:mm", Locale.getDefault()) }
    val formatterCorto    = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick        = onNavigateToRegistrar,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor   = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(painterResource(R.drawable.ic_add), contentDescription = "Agregar mascota")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier        = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            contentPadding  = PaddingValues(top = 20.dp, bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            // ── Header ──────────────────────────────────────────────────
            item {
                Text("SmartPaws", style = MaterialTheme.typography.headlineLarge)
                Text(
                    "¿Qué querés hacer hoy?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(24.dp))
            }

            // ── Próximo turno ────────────────────────────────────────────
            if (uiState.proximoEvento != null) {
                item {
                    Card(
                        modifier  = Modifier.fillMaxWidth(),
                        shape     = RoundedCornerShape(16.dp),
                        colors    = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier          = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier         = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter            = painterResource(R.drawable.ic_date_range),
                                    contentDescription = null,
                                    tint               = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Spacer(Modifier.width(14.dp))
                            Column {
                                Text(
                                    "Próximo turno",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                                )
                                Text(
                                    uiState.proximoEvento!!.titulo,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Text(
                                    "${uiState.proximoEventoMascotaNombre} · ${formatterCompleto.format(Date(uiState.proximoEvento!!.fecha))}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }
            }

            // ── Sección: Tus mascotas ────────────────────────────────────
            item {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        "Tus mascotas",
                        style      = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${uiState.cantidadMascotas} registradas",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(12.dp))
            }

            if (uiState.mascotasConTurno.isEmpty()) {
                item {
                    Box(
                        modifier         = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter            = painterResource(R.drawable.ic_pets),
                                contentDescription = null,
                                modifier           = Modifier.size(56.dp),
                                tint               = MaterialTheme.colorScheme.primaryContainer
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                "Aún no tenés mascotas",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "Tocá + para agregar la primera",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(uiState.mascotasConTurno) { item ->
                    MascotaResumenCard(item = item)
                    Spacer(Modifier.height(10.dp))
                }
            }

            // ── Sección: Próximos eventos ────────────────────────────────
            if (uiState.proximosEventos.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Próximos eventos",
                        style      = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))
                }

                items(uiState.proximosEventos) { (evento, nombreMascota) ->
                    ProximoEventoCard(
                        evento        = evento,
                        nombreMascota = nombreMascota,
                        formatter     = formatterCorto
                    )
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun MascotaResumenCard(
    item: MascotaConProximoTurno
) {
    val formatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Avatar + datos
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier              = Modifier.weight(1f)
            ) {
                Box(
                    modifier         = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter  = painterResource(R.drawable.ic_pets),
                        contentDescription = null,
                        tint     = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(
                        item.mascota.nombre,
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (item.proximoTurno != null) {
                        Text(
                            "Turno: ${formatter.format(Date(item.proximoTurno.fecha))}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                            "${item.mascota.especie} · Sin turnos",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProximoEventoCard(
    evento: EventoMascota,
    nombreMascota: String,
    formatter: SimpleDateFormat
) {
    val iconRes = if (evento.tipo == TipoEvento.TURNO_VETERINARIO)
        R.drawable.ic_date_range
    else
        R.drawable.ic_pets

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier          = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier         = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter            = painterResource(iconRes),
                    contentDescription = null,
                    tint               = MaterialTheme.colorScheme.primary,
                    modifier           = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    evento.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (evento.descripcion.isNotBlank()) {
                    Text(
                        evento.descripcion,
                        style  = MaterialTheme.typography.bodySmall,
                        color  = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    "$nombreMascota · ${formatter.format(Date(evento.fecha))}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalendarioTab(viewModel: CalendarioViewModel) {
    val eventos      by viewModel.eventos.collectAsState()
    val mascotas     by viewModel.mascotas.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val visibleMonth by viewModel.visibleMonth.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val (year, month) = visibleMonth

    // Contar eventos por día del mes visible
    val eventCountByDay: Map<Int, Int> = remember(eventos, year, month) {
        val result = mutableMapOf<Int, Int>()
        val cal    = Calendar.getInstance()
        eventos.forEach { evento ->
            cal.timeInMillis = evento.fecha
            if (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH) == month) {
                val d = cal.get(Calendar.DAY_OF_MONTH)
                result[d] = (result[d] ?: 0) + 1
            }
        }
        result
    }

    // Eventos del día seleccionado
    val eventosDelDia = remember(eventos, selectedDate) {
        val selCal = Calendar.getInstance().apply { timeInMillis = selectedDate }
        eventos.filter { evento ->
            val evCal = Calendar.getInstance().apply { timeInMillis = evento.fecha }
            evCal.get(Calendar.YEAR)       == selCal.get(Calendar.YEAR) &&
                    evCal.get(Calendar.DAY_OF_YEAR) == selCal.get(Calendar.DAY_OF_YEAR)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick        = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor   = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(painterResource(R.drawable.ic_add), "Agregar evento")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // ── Calendario custom ────────────────────────────────────────
            CustomCalendar(
                year             = year,
                month            = month,
                selectedDate     = selectedDate,
                eventCountByDay  = eventCountByDay,
                onDaySelected    = { viewModel.selectDate(it) },
                onPreviousMonth  = { viewModel.previousMonth() },
                onNextMonth      = { viewModel.nextMonth() },
                modifier         = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            // ── Header eventos del día ───────────────────────────────────
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                val dayFormatter = remember { SimpleDateFormat("dd 'de' MMMM", Locale.getDefault()) }
                Text(
                    text       = dayFormatter.format(Date(selectedDate))
                        .replaceFirstChar { it.uppercase() },
                    style      = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text  = "${eventosDelDia.size} evento${if (eventosDelDia.size != 1) "s" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // ── Lista eventos del día ────────────────────────────────────
            if (eventosDelDia.isEmpty()) {
                Box(
                    modifier         = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter            = painterResource(R.drawable.ic_date_range),
                            contentDescription = null,
                            modifier           = Modifier.size(48.dp),
                            tint               = MaterialTheme.colorScheme.primaryContainer
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Sin eventos este día",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier       = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start  = 16.dp,
                        end    = 16.dp,
                        bottom = 88.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(eventosDelDia) { ev ->
                        EventItem(
                            evento        = ev,
                            mascotaNombre = mascotas.find { it.id == ev.mascotaId }?.nombre ?: "Mascota"
                        )
                    }
                }
            }
        }
    }

    // ── Dialogs ──────────────────────────────────────────────────────────
    if (showAddDialog) {
        if (mascotas.isEmpty()) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title            = { Text("Sin mascotas") },
                text             = { Text("Registrá una mascota primero.") },
                confirmButton    = {
                    TextButton(onClick = { showAddDialog = false }) { Text("OK") }
                }
            )
        } else {
            AddEventoDialog(
                mascotas  = mascotas,
                onDismiss = { showAddDialog = false }
            ) { t, d, mId, tipo ->
                viewModel.addEvento(
                    EventoMascota(
                        mascotaId   = mId,
                        fecha       = selectedDate,
                        titulo      = t,
                        descripcion = d,
                        tipo        = tipo
                    )
                )
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