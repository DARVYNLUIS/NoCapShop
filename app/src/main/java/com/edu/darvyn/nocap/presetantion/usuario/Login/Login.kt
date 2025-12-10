package com.edu.darvyn.nocap.presetantion.usuario.Login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.darvyn.nocap.R
import com.edu.darvyn.nocap.presetantion.usuario.PerfilUsuario.PerfilUsuarioScreen

@Composable
fun LoginDecisionScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    goToBack: () -> Unit,
    goToCreate: () -> Unit,
    goToOrdenesCompras: (Int) -> Unit,
    goToPedidos: (Int) -> Unit,
    goToPanelAdmin: (Int) -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    if(state.isExiste){
        PerfilUsuarioScreen(
            onBack = goToBack,
            goToOrdenesCompras = goToOrdenesCompras,
            goToPedidos = goToPedidos,
            goToPanelAdmin = goToPanelAdmin
        )
    }else{
        Login(
            state,
            viewModel::onEvent,
            goToBack,
            goToRegister = goToCreate
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    state : LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
    goToBack: () -> Unit,
    goToRegister: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = goToBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "Volver",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        "Iniciar sesión",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            if (state.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Logo
            Icon(
                painter = painterResource(id = R.drawable.logo_no_cap),
                contentDescription = "Logo",
                modifier = Modifier.size(80.dp),
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Campo Email
            OutlinedTextField(
                value = state.email ?: "",
                onValueChange = { onEvent(LoginUiEvent.NombreChange(it)) },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !state.isLoading,
                shape = RoundedCornerShape(
                    topStart = 8.dp,
                    topEnd = 8.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            OutlinedTextField(
                value = state.password ?: "",
                onValueChange = { onEvent(LoginUiEvent.PasswordChange(it)) },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !state.isLoading,
                visualTransformation = if (state.isVisiblepassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { onEvent(LoginUiEvent.IsVisbleChange(!state.isVisiblepassword)) },
                        enabled = !state.isLoading
                    ) {
                        Icon(
                            imageVector = if (state.isVisiblepassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (state.isVisiblepassword) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onEvent(LoginUiEvent.IniciarSesion) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !state.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (state.isLoading) "Iniciando sesión..." else "Iniciar sesión",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿No tienes una cuenta? ",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
                TextButton(
                    onClick = goToRegister,
                    enabled = !state.isLoading
                ) {
                    Text(
                        text = "Crear cuenta",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}