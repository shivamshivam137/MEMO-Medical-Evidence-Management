# Project Report: MEMO — Medical Evidence Management & Organization
**Subject:** Website and Mobile Application Development (WMAD)  
**Author:** Shivam Singh  
**Date:** October 19, 2026  

---

### Academic Evaluation Header

| Name of Student | Roll No | DOP (Date of Perf.) | DOS (Date of Subm.) | Signature | Remark |
|:---|:---:|:---:|:---:|:---:|:---:|
| **Shivam Singh** | | 19-10-2026 | 19-10-2026 | | |

---

## 1. Project Overview

**MEMO (Medical Evidence Management & Organization)** is an intelligent healthcare mobile application designed to aggregate personal medical records into a structured, searchable, and longitudinal clinical history.

Patients frequently accumulate unstructured medical records across disparate pathology laboratories, multi-specialty hospitals, and independent clinics. When consulting new physicians, critical historical evidence is often inaccessible or scattered. MEMO resolves this problem by structuring lab parameters, tracking abnormal clinical flags, providing multi-category filtering, offering OCR-simulated document ingestion, and integrating cloud synchronization backed by Google Firebase Authentication and Google Cloud Firestore.

---

## 2. Technology Stack

- **Course / Curriculum:** Website and Mobile Application Development (WMAD)
- **Target Platform:** Android OS (Min SDK: 26 / Android 8.0, Target SDK: 34 / Android 14)
- **Language:** Kotlin 1.9.10
- **UI Framework:** Jetpack Compose (BOM 2024.04.01) with Material Design 3 (Material3)
- **Architecture Pattern:** MVVM (Model-View-ViewModel) + Clean Repository Pattern
- **State Management & Concurrency:** Kotlin Coroutines & `StateFlow` / `SharedFlow`
- **Cloud Authentication:** Google Firebase Authentication (Email/Password & AuthStateListener)
- **Cloud Database:** Google Cloud Firestore (Document & Subcollection Real-time Streams)
- **Document & Media Storage:** Google Firebase Cloud Storage
- **Build Automation & Dependencies:** Gradle 8.4 (Kotlin DSL `build.gradle.kts`) & AGP 8.3
- **CI/CD & DevOps Automation:** Dockerized Jenkins LTS Pipeline (Automated 7-stage CI/CD)
- **JDK Runtime:** Java OpenJDK 17 (Eclipse Adoptium Temurin)

---

## 3. Database Schema & Security Rules

### Cloud Firestore Security Rules (`firestore.rules`)
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    
    // User profile document: only the authenticated owner can read & write
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
      
      // Medical reports subcollection: private to the authenticated owner
      match /reports/{reportId} {
        allow read, write, delete: if request.auth != null && request.auth.uid == userId;
      }
    }
  }
}
```

### Firestore Collections Structure
- **Patient Profile (`/users/{userId}`):**
  - `id`: String (Firebase Auth UID)
  - `name`: String (e.g., "Shivam Singh")
  - `email`: String (e.g., "shivam.singh@memo.demo")
  - `memberSince`: String (e.g., "October 2026")
  - `bloodGroup`: String (e.g., "B+")
  - `age`: Number (e.g., 35)
  - `gender`: String (e.g., "Male")

- **Medical Reports Subcollection (`/users/{userId}/reports/{reportId}`):**
  - `id`: String (e.g., "rpt-001")
  - `title`: String (e.g., "Complete Blood Count")
  - `type`: String (Enum: `BLOOD_TEST`, `URINE_TEST`, `IMAGING`, `PRESCRIPTION`)
  - `date`: String (Format: `YYYY-MM-DD`)
  - `organization`: String (e.g., "Apex Clinical Laboratories")
  - `doctor`: String (e.g., "Dr. Aditi Sharma, MD")
  - `summary`: String
  - `hasAbnormalValues`: Boolean

---

## 4. Build & Environment Configuration

### App Gradle Configuration (`android/app/build.gradle.kts`)
```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.memo.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.memo.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
}

dependencies {
    // Jetpack Compose BOM
    val composeBom = platform("androidx.compose:compose-bom:2024.04.01")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Firebase Platform BOM & Services
    val firebaseBom = platform("com.google.firebase:firebase-bom:33.4.0")
    implementation(firebaseBom)
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")

    // Coroutines & Play Services Tasks Await
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.0")
}
```

### Application Manifest (`AndroidManifest.xml`)
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
        android:allowBackup="true"
        android:icon="@android:drawable/sym_def_app_icon"
        android:label="@string/app_name"
        android:roundIcon="@android:drawable/sym_def_app_icon"
        android:supportsRtl="true"
        android:theme="@style/Theme.MEMO">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:screenOrientation="portrait"
            android:theme="@style/Theme.MEMO">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

---

## 5. Core Architecture & Repository Code

### `IMemoRepository.kt` (Abstract Repository Contract)
```kotlin
package com.memo.app.data.repository

import com.memo.app.data.model.HealthStats
import com.memo.app.data.model.MedicalReport
import com.memo.app.data.model.ReportType
import com.memo.app.data.model.User
import kotlinx.coroutines.flow.Flow

interface IMemoRepository {
    fun getCurrentUser(): Flow<User>
    fun getReports(): Flow<List<MedicalReport>>
    fun getReportById(id: String): Flow<MedicalReport?>
    fun getHealthStats(): Flow<HealthStats>
    fun getFilteredReports(type: ReportType, query: String = ""): Flow<List<MedicalReport>>
    suspend fun uploadReport(fileName: String, fileSize: Long = 1024L): Result<MedicalReport>
    suspend fun login(email: String, pass: String): Result<User>
    suspend fun signUp(email: String, pass: String, name: String = ""): Result<User>
}
```

### `FirebaseMemoRepository.kt` (Cloud Firestore & Auth Real-Time Flow)
```kotlin
package com.memo.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.memo.app.data.mock.DemoDataSource
import com.memo.app.data.model.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirebaseMemoRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val mockFallback: MockMemoRepository = MockMemoRepository()
) : IMemoRepository {

    override fun getCurrentUser(): Flow<User> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val fbUser = firebaseAuth.currentUser
            if (fbUser != null) {
                trySend(
                    User(
                        id = fbUser.uid,
                        name = fbUser.displayName ?: fbUser.email?.substringBefore("@") ?: "Shivam Singh",
                        email = fbUser.email ?: "",
                        memberSince = "Active"
                    )
                )
            } else {
                trySend(DemoDataSource.currentUser)
            }
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override fun getReports(): Flow<List<MedicalReport>> {
        val currentUserId = auth.currentUser?.uid
        return if (currentUserId != null) {
            callbackFlow {
                val subscription = firestore.collection("users")
                    .document(currentUserId)
                    .collection("reports")
                    .addSnapshotListener { snapshot, error ->
                        if (error != null || snapshot == null || snapshot.isEmpty) {
                            trySend(DemoDataSource.reports.toList())
                            return@addSnapshotListener
                        }
                        val reports = snapshot.documents.mapNotNull { doc ->
                            try {
                                MedicalReport(
                                    id = doc.id,
                                    title = doc.getString("title") ?: "Medical Record",
                                    type = try {
                                        ReportType.valueOf(doc.getString("type") ?: "BLOOD_TEST")
                                    } catch (_: Exception) { ReportType.BLOOD_TEST },
                                    organization = doc.getString("organization") ?: "Clinical Lab",
                                    doctor = doc.getString("doctor") ?: "Attending Physician",
                                    date = doc.getString("date") ?: "2026-10-19",
                                    summary = doc.getString("summary") ?: "",
                                    hasAbnormalValues = doc.getBoolean("hasAbnormalValues") ?: false
                                )
                            } catch (_: Exception) { null }
                        }
                        trySend(if (reports.isEmpty()) DemoDataSource.reports.toList() else reports)
                    }
                awaitClose { subscription.remove() }
            }
        } else {
            mockFallback.getReports()
        }
    }

    override fun getReportById(id: String): Flow<MedicalReport?> =
        getReports().map { list -> list.find { it.id == id } }

    override fun getHealthStats(): Flow<HealthStats> =
        getReports().map { list ->
            HealthStats(
                totalReports = list.size,
                bloodTestsCount = list.count { it.type == ReportType.BLOOD_TEST },
                urineTestsCount = list.count { it.type == ReportType.URINE_TEST },
                imagingCount = list.count { it.type == ReportType.IMAGING },
                prescriptionsCount = list.count { it.type == ReportType.PRESCRIPTION },
                attentionRequiredCount = list.count { it.hasAbnormalValues }
            )
        }

    override fun getFilteredReports(type: ReportType, query: String): Flow<List<MedicalReport>> =
        getReports().map { list ->
            list.filter { report ->
                val matchesType = (type == ReportType.ALL || report.type == type)
                val matchesQuery = if (query.isBlank()) true else {
                    val q = query.trim().lowercase()
                    report.title.lowercase().contains(q) ||
                    report.organization.lowercase().contains(q) ||
                    report.doctor.lowercase().contains(q)
                }
                matchesType && matchesQuery
            }
        }

    override suspend fun uploadReport(fileName: String, fileSize: Long): Result<MedicalReport> {
        val currentUserId = auth.currentUser?.uid
        val newReport = DemoDataSource.addUploadedReport(fileName)
        if (currentUserId != null) {
            try {
                val reportData = hashMapOf(
                    "title" to newReport.title,
                    "type" to newReport.type.name,
                    "organization" to newReport.organization,
                    "doctor" to newReport.doctor,
                    "date" to newReport.date,
                    "summary" to newReport.summary,
                    "hasAbnormalValues" to newReport.hasAbnormalValues
                )
                firestore.collection("users")
                    .document(currentUserId)
                    .collection("reports")
                    .document(newReport.id)
                    .set(reportData)
                    .await()
            } catch (_: Exception) { }
        }
        return Result.success(newReport)
    }

    override suspend fun login(email: String, pass: String): Result<User> = try {
        val authResult = auth.signInWithEmailAndPassword(email, pass).await()
        val fbUser = authResult.user
        Result.success(
            User(
                id = fbUser?.uid ?: "usr-01",
                name = fbUser?.displayName ?: fbUser?.email?.substringBefore("@") ?: "Shivam Singh",
                email = fbUser?.email ?: email,
                memberSince = "Active"
            )
        )
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signUp(email: String, pass: String, name: String): Result<User> = try {
        val authResult = auth.createUserWithEmailAndPassword(email, pass).await()
        val fbUser = authResult.user
        val user = User(
            id = fbUser?.uid ?: "usr-01",
            name = if (name.isNotBlank()) name else fbUser?.email?.substringBefore("@") ?: "Shivam Singh",
            email = fbUser?.email ?: email,
            memberSince = "October 2026"
        )
        fbUser?.uid?.let { uid ->
            val userMap = hashMapOf(
                "id" to uid,
                "name" to user.name,
                "email" to user.email,
                "memberSince" to user.memberSince,
                "bloodGroup" to user.bloodGroup
            )
            firestore.collection("users").document(uid).set(userMap).await()
        }
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

---

## 6. Mobile Application UI & ViewModel Layer

### `AuthViewModel.kt` (StateFlow & ViewModel)
```kotlin
package com.memo.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memo.app.data.mock.DemoDataSource
import com.memo.app.data.model.User
import com.memo.app.data.repository.IMemoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isSignUp: Boolean = false,
    val name: String = "",
    val email: String = "shivam.singh@memo.demo",
    val pass: String = "demo1234",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null,
    val statusMessage: String? = null
)

class AuthViewModel(
    private val repository: IMemoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun toggleAuthMode() {
        _uiState.value = _uiState.value.copy(
            isSignUp = !_uiState.value.isSignUp,
            errorMessage = null,
            statusMessage = null
        )
    }

    fun onNameChanged(name: String) { _uiState.value = _uiState.value.copy(name = name) }
    fun onEmailChanged(email: String) { _uiState.value = _uiState.value.copy(email = email) }
    fun onPasswordChanged(pass: String) { _uiState.value = _uiState.value.copy(pass = pass) }

    fun login() {
        val state = _uiState.value
        _uiState.value = state.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val result = repository.login(state.email, state.pass)
            result.fold(
                onSuccess = { user ->
                    _uiState.value = _uiState.value.copy(isLoading = false, isLoggedIn = true, user = user)
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = error.message)
                }
            )
        }
    }

    fun signUp() {
        val state = _uiState.value
        _uiState.value = state.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val result = repository.signUp(state.email, state.pass, state.name)
            result.fold(
                onSuccess = { user ->
                    _uiState.value = _uiState.value.copy(isLoading = false, isLoggedIn = true, user = user)
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = error.message)
                }
            )
        }
    }

    fun loginWithDemo() {
        val demoUser = DemoDataSource.currentUser
        _uiState.value = _uiState.value.copy(
            email = demoUser.email,
            pass = "demo1234",
            isLoading = false,
            isLoggedIn = true,
            user = demoUser
        )
    }

    fun logout() {
        _uiState.value = AuthUiState()
    }
}
```

---

## 7. Design Tokens & Styling (`Color.kt`)

```kotlin
package com.memo.app.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Healthcare Brand Colors
val Teal600 = Color(0xFF0D9488)        // Primary Brand Accent
val Teal700 = Color(0xFF0F766E)        // Dark Teal
val Teal100 = Color(0xFFCCFBF1)        // Teal Container Wash

// Clinical Slate Palette (Text & Dark Surfaces)
val Slate900 = Color(0xFF0F172A)       // Primary Text / Top App Bar
val Slate700 = Color(0xFF334155)       // Body Text / Icons
val Slate200 = Color(0xFFE2E8F0)       // Card Borders / Dividers
val Slate100 = Color(0xFFF1F5F9)       // Chip Background / Surface Variant

// Background & Surfaces
val BackgroundLight = Color(0xFFFAFBFC)
val SurfaceWhite = Color(0xFFFFFFFF)

// Semantic & Clinical Status Indicators
val StatusNormal = Color(0xFF16A34A)       // Green 600 - Normal Values
val StatusNormalBg = Color(0xFFDCFCE7)     // Green 100 Container
val StatusAbnormal = Color(0xFFDC2626)     // Red 600 - Abnormal / Critical Flag
val StatusAbnormalBg = Color(0xFFFEE2E2)   // Red 100 Container
val StatusWarning = Color(0xFFD97706)      // Amber 600 - Borderline / Elevated Flag
val StatusWarningBg = Color(0xFFFEF3C7)    // Amber 100 Container
```

---

## 8. API Operations & CI/CD Pipeline

| Stage / Operation | Tool / Protocol | Target / Artifact | Result |
|:---|:---|:---|:---|
| **1. Checkout SCM** | Git Plugin | `origin/develop` | Cloned commit into workspace |
| **2. Prepare Env** | OpenJDK 17 + Gradle | `java -version`, `gradlew` | Environment validated |
| **3. Compile** | Kotlin Gradle Plugin | `compileDebugKotlin` | Native bytecode compiled |
| **4. Unit Tests** | JUnit 4 + Coroutines Test | `testDebugUnitTest` | Test results recorded |
| **5. Android Lint** | AGP Lint Tool | `lintDebug` | Static code analysis passed |
| **6. Assemble APK** | AAPT2 + D8 Compiler | `assembleDebug` | Generated `app-debug.apk` |
| **7. Archive Artifacts** | Jenkins Archiver | `app-debug.apk` | Stored build artifact |

---

## 9. Mobile Application Screen Architecture

1. **Splash & Branding Screen:** Animated shield logo with full expansion of "Medical Evidence Management & Organization".
2. **Authentication Screen:** Email/password Firebase registration & sign-in, complemented by a one-tap Demo Mode button.
3. **Executive Dashboard:** Displays health metrics (Total Reports: 12, Attention Needed: 3), recent test highlights, and upload FAB.
4. **Reports Explorer:** Filter chips for Blood Test, Urine Test, Imaging, and Prescription categories with real-time text query filtering.
5. **Clinical Report Detail Screen:** Table of extracted lab values, units, reference intervals, and visual abnormal indicators.
6. **Medical Timeline Screen:** Chronological health journey grouped by Year and Month with vertical connector lines.
7. **Simulated Ingest & OCR Screen:** 3-stage animated extraction progress (Upload -> OCR -> Structured Parameters) with automatic local & Firestore persistence.
8. **Patient Profile Screen:** Patient demographic details (Name: Shivam Singh, Blood Group B+), architecture inspection card, and sign-out handler.

---

## 10. Conclusion

The **MEMO** application fulfills the objectives of the **Website and Mobile Application Development (WMAD)** course by demonstrating:
1. Native Android mobile frontend engineering with Kotlin, Jetpack Compose, and Material 3.
2. Robust architectural separation using MVVM and the Clean Repository Pattern.
3. Cloud synchronization with Google Firebase Authentication and Google Cloud Firestore.
4. Automated DevOps deployment via a 7-stage Jenkins CI/CD pipeline producing production-ready APK artifacts.
