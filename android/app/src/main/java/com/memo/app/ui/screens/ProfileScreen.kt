package com.memo.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.memo.app.data.mock.DemoDataSource
import com.memo.app.data.model.User
import com.memo.app.ui.theme.*
import com.memo.app.viewmodel.AuthViewModel

private val BLOOD_GROUPS = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
private val GENDERS = listOf("Male", "Female", "Other")

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    val authState by authViewModel.uiState.collectAsState()
    val user = authState.user ?: DemoDataSource.currentUser

    var showEditDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    if (showEditDialog) {
        EditProfileDialog(
            currentUser = user,
            onDismiss = { showEditDialog = false },
            onSave = { name, nickname, bloodGroup, age, gender, emergencyContact, allergies ->
                authViewModel.updateProfile(
                    name = name,
                    nickname = nickname,
                    bloodGroup = bloodGroup,
                    age = age,
                    gender = gender,
                    emergencyContact = emergencyContact,
                    allergies = allergies
                ) {
                    showEditDialog = false
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Patient Profile",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Slate900
            )

            IconButton(
                onClick = { showEditDialog = true },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Teal50)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile",
                    tint = Teal700
                )
            }
        }

        // Profile Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            border = BorderStroke(1.dp, Slate200)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Teal100),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Teal700,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = user.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )

                if (user.nickname.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Nickname: \"${user.nickname}\"",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Teal700
                    )
                }

                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate500
                )

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Slate100)
                Spacer(modifier = Modifier.height(16.dp))

                // Stats Grid: Blood Group, Age, Gender
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Blood Group", style = MaterialTheme.typography.bodySmall, color = Slate500)
                        Text(text = user.bloodGroup, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Slate900)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Age", style = MaterialTheme.typography.bodySmall, color = Slate500)
                        Text(text = "${user.age}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Slate900)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Gender", style = MaterialTheme.typography.bodySmall, color = Slate500)
                        Text(text = user.gender, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Slate900)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = Slate100)
                Spacer(modifier = Modifier.height(14.dp))

                // Additional Profile Details
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = Slate400, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Emergency Contact: ",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate500
                        )
                        Text(
                            text = user.emergencyContact.ifBlank { "Not specified" },
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = Slate800
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Slate400, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Allergies / Notes: ",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate500
                        )
                        Text(
                            text = user.allergies.ifBlank { "None reported" },
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = Slate800
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Edit Button
                OutlinedButton(
                    onClick = { showEditDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Teal700),
                    border = BorderStroke(1.dp, Teal600)
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Edit Profile Details")
                }
            }
        }

        // Firebase Sync & Seed Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            border = BorderStroke(1.dp, Slate200)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Firebase Cloud Database Sync",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )

                Text(
                    text = "Sync and populate sample longitudinal reports to your Firebase Firestore database.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate600
                )

                if (authState.statusMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = authState.statusMessage ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = Teal700,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (authState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = authState.errorMessage ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = StatusAbnormal,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { authViewModel.seedSampleData() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Teal700),
                    border = BorderStroke(1.dp, Teal600),
                    enabled = !authState.isLoading
                ) {
                    Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Seed 12 Sample Reports to Firestore")
                }
            }
        }

        // System Architecture Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            border = BorderStroke(1.dp, Slate200)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Backend & Security Architecture",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "• Authentication: Firebase Auth (Email & Password)\n• Data Storage: Google Cloud Firestore (Real-Time Streams)\n• Document Storage: Firebase Cloud Storage\n• Client Layer: MVVM + Jetpack Compose + Kotlin Coroutines Flow\n• Offline Cache: Local SharedPreferences + Firestore Persistence",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate700,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Logout Button
        Button(
            onClick = {
                authViewModel.logout()
                onLogout()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = StatusAbnormalBg, contentColor = StatusAbnormal)
        ) {
            Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null, tint = StatusAbnormal)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Sign Out Session",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = StatusAbnormal
            )
        }
    }
}

@Composable
fun EditProfileDialog(
    currentUser: User,
    onDismiss: () -> Unit,
    onSave: (
        name: String,
        nickname: String,
        bloodGroup: String,
        age: Int,
        gender: String,
        emergencyContact: String,
        allergies: String
    ) -> Unit
) {
    var name by remember { mutableStateOf(currentUser.name) }
    var nickname by remember { mutableStateOf(currentUser.nickname) }
    var ageText by remember { mutableStateOf(currentUser.age.toString()) }
    var bloodGroup by remember { mutableStateOf(currentUser.bloodGroup) }
    var gender by remember { mutableStateOf(currentUser.gender) }
    var emergencyContact by remember { mutableStateOf(currentUser.emergencyContact) }
    var allergies by remember { mutableStateOf(currentUser.allergies) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Edit Patient Profile",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )

                Text(
                    text = "Update your medical profile and personal preferences.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate600
                )

                Divider(color = Slate100)

                // Full Name
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        errorMessage = null
                    },
                    label = { Text("Full Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                // Nickname
                OutlinedTextField(
                    value = nickname,
                    onValueChange = {
                        nickname = it
                        errorMessage = null
                    },
                    label = { Text("Nickname (e.g. Shiv)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                // Age
                OutlinedTextField(
                    value = ageText,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() } && it.length <= 3) {
                            ageText = it
                            errorMessage = null
                        }
                    },
                    label = { Text("Age *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                // Gender
                Text(
                    text = "Gender",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate700
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    GENDERS.forEach { item ->
                        val isSelected = gender.equals(item, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Teal600 else Slate100)
                                .clickable { gender = item }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) SurfaceWhite else Slate700
                            )
                        }
                    }
                }

                // Blood Group
                Text(
                    text = "Blood Group *",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate700
                )

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        BLOOD_GROUPS.take(4).forEach { bg ->
                            val isSelected = bloodGroup.equals(bg, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) Teal600 else Slate100)
                                    .clickable { bloodGroup = bg }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = bg,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) SurfaceWhite else Slate800
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        BLOOD_GROUPS.drop(4).forEach { bg ->
                            val isSelected = bloodGroup.equals(bg, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) Teal600 else Slate100)
                                    .clickable { bloodGroup = bg }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = bg,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) SurfaceWhite else Slate800
                                )
                            }
                        }
                    }
                }

                // Emergency Contact
                OutlinedTextField(
                    value = emergencyContact,
                    onValueChange = { emergencyContact = it },
                    label = { Text("Emergency Contact") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                // Allergies / Medical Notes
                OutlinedTextField(
                    value = allergies,
                    onValueChange = { allergies = it },
                    label = { Text("Allergies / Special Notes") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = StatusAbnormal,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Dialog Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val parsedAge = ageText.toIntOrNull() ?: 0
                            if (name.isBlank()) {
                                errorMessage = "Name cannot be empty."
                            } else if (parsedAge <= 0 || parsedAge > 130) {
                                errorMessage = "Please enter a valid age."
                            } else {
                                onSave(name, nickname, bloodGroup, parsedAge, gender, emergencyContact, allergies)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Slate900)
                    ) {
                        Text("Save", color = SurfaceWhite)
                    }
                }
            }
        }
    }
}
