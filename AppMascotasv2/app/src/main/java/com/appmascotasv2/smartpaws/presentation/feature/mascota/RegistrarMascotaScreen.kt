package com.appmascotasv2.smartpaws.presentation.feature.mascota

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.appmascotasv2.smartpaws.R
import com.appmascotasv2.smartpaws.presentation.ui.components.SectionLabel
import com.appmascotasv2.smartpaws.presentation.feature.mascota.model.MascotaCatalog
import kotlin.collections.forEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarMascotaScreen (
    viewModel: com.appmascotasv2.smartpaws.presentation.feature.mascota.RegistrarMascotaViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var fechaCumpleanios by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }

    var expandedEspecie by remember { mutableStateOf(false) }
    var expandedRaza by remember { mutableStateOf(false) }

    val especies = remember { _root_ide_package_.com.appmascotasv2.smartpaws.presentation.feature.mascota.model.MascotaCatalog.especies }
    val razas = remember(especie) {
        _root_ide_package_.com.appmascotasv2.smartpaws.presentation.feature.mascota.model.MascotaCatalog.razasPorEspecie[especie] ?: emptyList()
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Nueva Mascota", style = MaterialTheme.typography.headlineMedium)
                },
                navigationIcon = {
                    Icon(
                        painter            = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = "Volver"
                    )
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
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            SectionLabel("Información básica")

            OutlinedTextField(
                value         = nombre,
                onValueChange = { nombre = it; viewModel.clearErrors() },
                label         = { Text("Nombre *") },
                leadingIcon   = {                     Icon(
                    painter            = painterResource(R.drawable.ic_badge),
                    contentDescription = null
                ) },
                isError       = uiState.nameError,
                supportingText = if (uiState.nameError) ({ Text("El nombre es requerido") }) else null,
                modifier      = Modifier.fillMaxWidth(),
                singleLine    = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                shape         = MaterialTheme.shapes.medium
            )


            ExposedDropdownMenuBox(
                expanded = expandedEspecie,
                onExpandedChange = {
                    expandedEspecie = !expandedEspecie
                }
            ) {
                OutlinedTextField(
                    value = especie,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Especie") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_pets),
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expandedEspecie
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = MaterialTheme.shapes.medium
                )

                ExposedDropdownMenu(
                    expanded = expandedEspecie,
                    onDismissRequest = {
                        expandedEspecie = false
                    }
                ) {
                    especies.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                especie = opcion
                                raza = ""
                                expandedEspecie = false
                            }
                        )
                    }
                }
            }

            if (especie.isNotBlank()) {

                ExposedDropdownMenuBox(
                    expanded = expandedRaza,
                    onExpandedChange = {
                        expandedRaza = !expandedRaza
                    }

                ) {

                    OutlinedTextField(
                        value = raza,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Raza") },

                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_pets),
                                contentDescription = null
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expandedRaza
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = MaterialTheme.shapes.medium
                    )

                    ExposedDropdownMenu(
                        expanded = expandedRaza,
                        onDismissRequest = {
                            expandedRaza = false
                        }
                    ) {

                        razas.forEach { opcion ->

                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    raza = opcion
                                    expandedRaza = false
                                }
                            )
                        }
                    }
                }
            }



            SectionLabel("Datos de salud")

            OutlinedTextField(
                value         = fechaCumpleanios,
                onValueChange = { input ->
                    val digits = input.filter { it.isDigit() }
                        .take(8)
                    fechaCumpleanios = buildString {
                        digits.forEachIndexed { index, c ->
                            append(c)
                            if (index == 1 || index == 3) {
                                append("/") }
                        }
                    } },
                label         = { Text("Fecha de nacimiento") },
                leadingIcon   = {                  Icon(
                    painter            = painterResource(R.drawable.ic_date_range),
                    contentDescription = null
                ) },
                placeholder   = { Text("DD/MM/YYYY") },
                modifier      = Modifier.fillMaxWidth(),
                singleLine    = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction    = ImeAction.Next
                ),
                shape = MaterialTheme.shapes.medium
            )

            OutlinedTextField(
                value         = peso,
                onValueChange = { peso = it; viewModel.clearErrors() },
                label         = { Text("Peso (kg)") },
                leadingIcon   = {                     Icon(
                    painter            = painterResource(R.drawable.ic_weight),
                    contentDescription = null
                ) },
                isError       = uiState.weightError,
                supportingText = if (uiState.weightError) ({ Text("Ingresá un peso válido") }) else null,
                modifier      = Modifier.fillMaxWidth(),
                singleLine    = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction    = ImeAction.Done
                ),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick  = { viewModel.register(nombre, especie, raza,fechaCumpleanios, peso) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Registrar mascota", style = MaterialTheme.typography.titleLarge)
            }

            Spacer(Modifier.height(24.dp))
        }
    }

}