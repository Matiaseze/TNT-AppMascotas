package com.appmascotasv2.smartpaws.feature.auth

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.appmascotasv2.smartpaws.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var username        by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Navegar cuando el login es exitoso
    LaunchedEffect(uiState.loggedUserId) {
        uiState.loggedUserId?.let { onLoginSuccess(it) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            horizontalAlignment   = Alignment.CenterHorizontally,
            verticalArrangement   = Arrangement.spacedBy(16.dp)
        ) {

            // Logo
            Box(
                modifier         = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter            = painterResource(R.drawable.ic_pets),
                    contentDescription = null,
                    tint               = MaterialTheme.colorScheme.primary,
                    modifier           = Modifier.size(40.dp)
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(
                text  = "AppMascotas",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            AnimatedContent(
                targetState = uiState.isRegisterMode,
                label       = "subtitle"
            ) { isRegister ->
                Text(
                    text      = if (isRegister) "Creá tu cuenta" else "Iniciá sesión para continuar",
                    style     = MaterialTheme.typography.bodyMedium,
                    color     = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(8.dp))

            // Error banner
            AnimatedVisibility(visible = uiState.error != null)  {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text     = uiState.error ?: "",
                        modifier = Modifier.padding(12.dp),
                        style    = MaterialTheme.typography.bodyMedium,
                        color    = MaterialTheme.colorScheme.error
                    )
                }
            }

            // Usuario
            OutlinedTextField(
                value         = username,
                onValueChange = { username = it; viewModel.clearError() },
                label         = { Text("Usuario") },
                leadingIcon   = { Icon(
                    painter = painterResource(R.drawable.ic_person),
                    contentDescription = null)
                                },
                modifier      = Modifier.fillMaxWidth(),
                singleLine    = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction    = ImeAction.Next
                ),
                shape = MaterialTheme.shapes.medium
            )

            // Contraseña
            OutlinedTextField(
                value         = password,
                onValueChange = { password = it; viewModel.clearError() },
                label         = { Text("Contraseña") },
                leadingIcon   = {                     Icon(
                    painter            = painterResource(R.drawable.ic_lock),
                    contentDescription = null
                ) },
                trailingIcon  = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(
                                    if (passwordVisible) R.drawable.ic_visibility_off
                                    else R.drawable.ic_visibility
                                ),
                                contentDescription = if (passwordVisible) "Ocultar" else "Mostrar"
                            )
                    }
                },
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                modifier      = Modifier.fillMaxWidth(),
                singleLine    = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction    = ImeAction.Done
                ),
                shape = MaterialTheme.shapes.medium
            )
            // Confirmar Contraseña
            AnimatedVisibility(
                visible = uiState.isRegisterMode
            ) {
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        viewModel.clearError()
                    },
                    label = { Text("Confirmar contraseña") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_lock),
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                confirmPasswordVisible = !confirmPasswordVisible
                            }
                        ) {
                            Icon(
                                painter = painterResource(
                                    if (confirmPasswordVisible)
                                        R.drawable.ic_visibility_off
                                    else
                                        R.drawable.ic_visibility
                                ),
                                contentDescription = null
                            )
                        }
                    },
                    visualTransformation =
                        if (confirmPasswordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            Spacer(Modifier.height(4.dp))

            // Botón principal
            Button(
                onClick  = {
                    if (uiState.isRegisterMode)
                        viewModel.register(username, password, confirmPassword)
                    else
                        viewModel.login(username, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled  = !uiState.isLoading,
                shape    = MaterialTheme.shapes.medium
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color    = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text  = if (uiState.isRegisterMode) "Crear cuenta" else "Ingresar",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            // Toggle login / registro
            TextButton(
                onClick = {
                    confirmPassword = ""
                    viewModel.toggleMode()
                }
            ) {
                Text(
                    text  = if (uiState.isRegisterMode)
                        "¿Tienes una cuenta? Iniciá sesión"
                    else
                        "¿No tenés cuenta?. Registrate!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Espacio para el boton de Google (Firebase a futuro)
            // Divider()
            // GoogleSignInButton(onClick = { viewModel.loginWithGoogle(...) })
        }
    }
}