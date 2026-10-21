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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.memo.app.ui.theme.*
import com.memo.app.viewmodel.AuthViewModel

private val BLOOD_GROUPS = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
private val GENDERS = listOf("Male", "Female", "Other")

@Composable
fun OnboardingProfileScreen(
    authViewModel: AuthViewModel,
    onProfileCompleted: () -> Unit
) {
    val state by authViewModel.uiState.collectAsState()
    val initialUser = state.user

    var name by remember(initialUser) { mutableStateOf(initialUser?.name ?: "") }
    var nickname by remember(initialUser) { mutableStateOf(initialUser?.nickname ?: "") }
    var ageText by remember(initialUser) { mutableStateOf(initialUser?.age?.toString() ?: "28") }
    var bloodGroup by remember(initialUser) { mutableStateOf(initialUser?.bloodGroup ?: "B+") }
    var gender by remember(initialUser) { mutableStateOf(initialUser?.gender ?: "Male") }
    var emergencyContact by remember(initialUser) { mutableStateOf(initialUser?.emergencyContact ?: "") }
    var allergies by remember(initialUser) { mutableStateOf(initialUser?.allergies ?: "None reported") }
    var validationError by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Badge
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(Teal100),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = null,
                    tint = Teal700,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Welcome to MEMO 👋",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Slate900
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Please set up your patient profile once to personalize your medical records and emergency summary.",
                style = MaterialTheme.typography.bodyMedium,
                color = Slate600,
                lineHeight = 20.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                border = BorderStroke(1.dp, Slate200),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Personal Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )

                    // Full Name
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            validationError = null
                        },
                        label = { Text("Full Name *") },
                        placeholder = { Text("e.g. Shivam Singh") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Slate400)
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Nickname
                    OutlinedTextField(
                        value = nickname,
                        onValueChange = {
                            nickname = it
                            validationError = null
                        },
                        label = { Text("Nickname / Preferred Call Name") },
                        placeholder = { Text("e.g. Shiv, Patient") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Badge, contentDescription = null, tint = Slate400)
                        },
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
                                validationError = null
                            }
                        },
                        label = { Text("Age (Years) *") },
                        placeholder = { Text("e.g. 28") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null, tint = Slate400)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Gender Selector
                    Text(
                        text = "Gender",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate700
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        GENDERS.forEach { item ->
                            val isSelected = gender.equals(item, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) Teal600 else Slate100)
                                    .clickable { gender = item }
                                    .padding(vertical = 10.dp),
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

                    Divider(color = Slate100, modifier = Modifier.padding(vertical = 4.dp))

                    // Blood Group Selector
                    Text(
                        text = "Blood Group *",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate700
                    )

                    // 4 columns grid for 8 blood groups
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            BLOOD_GROUPS.take(4).forEach { bg ->
                                val isSelected = bloodGroup.equals(bg, ignoreCase = true)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) Teal600 else Slate100)
                                        .clickable { bloodGroup = bg }
                                        .padding(vertical = 10.dp),
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
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            BLOOD_GROUPS.drop(4).forEach { bg ->
                                val isSelected = bloodGroup.equals(bg, ignoreCase = true)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) Teal600 else Slate100)
                                        .clickable { bloodGroup = bg }
                                        .padding(vertical = 10.dp),
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

                    Divider(color = Slate100, modifier = Modifier.padding(vertical = 4.dp))

                    // Emergency Contact
                    OutlinedTextField(
                        value = emergencyContact,
                        onValueChange = { emergencyContact = it },
                        label = { Text("Emergency Contact (Optional)") },
                        placeholder = { Text("+91 98765 43210") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = Slate400)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Allergies / Special Conditions
                    OutlinedTextField(
                        value = allergies,
                        onValueChange = { allergies = it },
                        label = { Text("Known Allergies / Medical Notes") },
                        placeholder = { Text("e.g. Penicillin, Peanuts, None") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Slate400)
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (validationError != null) {
                        Text(
                            text = validationError ?: "",
                            color = StatusAbnormal,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (state.errorMessage != null) {
                        Text(
                            text = state.errorMessage ?: "",
                            color = StatusAbnormal,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Save Button
                    Button(
                        onClick = {
                            val parsedAge = ageText.toIntOrNull() ?: 0
                            if (name.isBlank()) {
                                validationError = "Please enter your name."
                            } else if (parsedAge <= 0 || parsedAge > 130) {
                                validationError = "Please enter a valid age (1 to 130)."
                            } else if (bloodGroup.isBlank()) {
                                validationError = "Please select your blood group."
                            } else {
                                validationError = null
                                authViewModel.updateProfile(
                                    name = name,
                                    nickname = nickname,
                                    bloodGroup = bloodGroup,
                                    age = parsedAge,
                                    gender = gender,
                                    emergencyContact = emergencyContact,
                                    allergies = allergies,
                                    onComplete = onProfileCompleted
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
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
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = SurfaceWhite)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Save & Continue to Dashboard",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = SurfaceWhite
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "💡 You can edit your blood group, age, nickname, and contacts anytime from the Profile tab.",
                style = MaterialTheme.typography.bodySmall,
                color = Slate500,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
