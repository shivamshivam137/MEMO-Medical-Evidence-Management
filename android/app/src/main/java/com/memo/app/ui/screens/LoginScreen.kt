package com.memo.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.memo.app.ui.theme.*
import com.memo.app.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: (isFirstTime: Boolean) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    androidx.compose.runtime.LaunchedEffect(state.isLoggedIn, state.isFirstTimeUser) {
        if (state.isLoggedIn) {
            onLoginSuccess(state.isFirstTimeUser)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = null,
                    tint = Teal600,
                    modifier = Modifier.size(44.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "MEMO Medical Archive",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )

                Text(
                    text = "Medical Evidence Management & Organization",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Teal600
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Auth Mode Switcher (Sign In vs Create Account)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Slate100)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (!state.isSignUp) SurfaceWhite else Slate100)
                            .clickable { if (state.isSignUp) viewModel.toggleAuthMode() }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sign In",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (!state.isSignUp) FontWeight.Bold else FontWeight.Medium,
                            color = if (!state.isSignUp) Slate900 else Slate500
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (state.isSignUp) SurfaceWhite else Slate100)
                            .clickable { if (!state.isSignUp) viewModel.toggleAuthMode() }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Create Account",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (state.isSignUp) FontWeight.Bold else FontWeight.Medium,
                            color = if (state.isSignUp) Slate900 else Slate500
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name field only during registration
                if (state.isSignUp) {
                    OutlinedTextField(
                        value = state.name,
                        onValueChange = { viewModel.onNameChanged(it) },
                        label = { Text("Full Name") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Slate400)
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                OutlinedTextField(
                    value = state.email,
                    onValueChange = { viewModel.onEmailChanged(it) },
                    label = { Text("Firebase Email") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = Slate400)
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = state.pass,
                    onValueChange = { viewModel.onPasswordChanged(it) },
                    label = { Text("Password (min 6 characters)") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = Slate400)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                if (state.errorMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = state.errorMessage ?: "",
                        color = StatusAbnormal,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (state.statusMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = state.statusMessage ?: "",
                        color = Teal700,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Primary Action Button (Sign In or Sign Up)
                Button(
                    onClick = {
                        if (state.isSignUp) {
                            viewModel.signUp()
                        } else {
                            viewModel.login()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Slate900),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            color = SurfaceWhite,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = if (state.isSignUp) "Create Firebase Account" else "Sign In with Firebase",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = SurfaceWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // One-tap demo button for rapid offline demonstration
                OutlinedButton(
                    onClick = { viewModel.loginWithDemo() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Teal700),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(Teal600))
                ) {
                    Text(
                        text = "Demo Mode (Offline Clinical Records)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Teal700
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "🔒 Backed by Firebase Authentication & Cloud Firestore",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate400,
                    fontSize = 11.sp
                )
            }
        }
    }
}
