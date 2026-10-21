package com.memo.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.memo.app.data.local.UserProfilePreferences
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
                firestore.collection("users").document(fbUser.uid)
                    .addSnapshotListener { snapshot, _ ->
                        if (snapshot != null && snapshot.exists()) {
                            val user = User(
                                id = fbUser.uid,
                                name = snapshot.getString("name") ?: fbUser.displayName ?: fbUser.email?.substringBefore("@") ?: "MEMO Patient",
                                email = snapshot.getString("email") ?: fbUser.email ?: "",
                                memberSince = snapshot.getString("memberSince") ?: "Active",
                                bloodGroup = snapshot.getString("bloodGroup") ?: "B+",
                                age = snapshot.getLong("age")?.toInt() ?: 35,
                                gender = snapshot.getString("gender") ?: "Male",
                                nickname = snapshot.getString("nickname") ?: "",
                                emergencyContact = snapshot.getString("emergencyContact") ?: "",
                                allergies = snapshot.getString("allergies") ?: "",
                                isProfileCompleted = snapshot.getBoolean("isProfileCompleted") ?: UserProfilePreferences.isProfileCompleted()
                            )
                            UserProfilePreferences.saveUser(user)
                            trySend(user)
                        } else {
                            val fallback = UserProfilePreferences.loadUser(
                                User(
                                    id = fbUser.uid,
                                    name = fbUser.displayName ?: fbUser.email?.substringBefore("@") ?: "MEMO Patient",
                                    email = fbUser.email ?: "",
                                    memberSince = "Active",
                                    bloodGroup = "B+",
                                    age = 35,
                                    gender = "Male",
                                    isProfileCompleted = UserProfilePreferences.isProfileCompleted()
                                )
                            )
                            trySend(fallback)
                        }
                    }
            } else {
                trySend(UserProfilePreferences.loadUser(DemoDataSource.currentUser))
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
                                    } catch (_: Exception) {
                                        ReportType.BLOOD_TEST
                                    },
                                    organization = doc.getString("organization") ?: "Clinical Lab",
                                    doctor = doc.getString("doctor") ?: "Attending Physician",
                                    date = doc.getString("date") ?: "2026-10-19",
                                    summary = doc.getString("summary") ?: "",
                                    hasAbnormalValues = doc.getBoolean("hasAbnormalValues") ?: false
                                )
                            } catch (_: Exception) {
                                null
                            }
                        }
                        trySend(if (reports.isEmpty()) DemoDataSource.reports.toList() else reports)
                    }
                awaitClose { subscription.remove() }
            }
        } else {
            mockFallback.getReports()
        }
    }

    override fun getReportById(id: String): Flow<MedicalReport?> {
        return getReports().map { list -> list.find { it.id == id } }
    }

    override fun getHealthStats(): Flow<HealthStats> {
        return getReports().map { list ->
            HealthStats(
                totalReports = list.size,
                bloodTestsCount = list.count { it.type == ReportType.BLOOD_TEST },
                urineTestsCount = list.count { it.type == ReportType.URINE_TEST },
                imagingCount = list.count { it.type == ReportType.IMAGING },
                prescriptionsCount = list.count { it.type == ReportType.PRESCRIPTION },
                attentionRequiredCount = list.count { it.hasAbnormalValues }
            )
        }
    }

    override fun getFilteredReports(type: ReportType, query: String): Flow<List<MedicalReport>> {
        return getReports().map { list ->
            list.filter { report ->
                val matchesType = (type == ReportType.ALL || report.type == type)
                val matchesQuery = if (query.isBlank()) true else {
                    val q = query.trim().lowercase()
                    report.title.lowercase().contains(q) ||
                    report.organization.lowercase().contains(q) ||
                    report.doctor.lowercase().contains(q) ||
                    report.labResults.any { it.testName.lowercase().contains(q) } ||
                    report.medications.any { it.name.lowercase().contains(q) }
                }
                matchesType && matchesQuery
            }
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
            } catch (_: Exception) {
                // Return locally created report on network fallback
            }
        }
        return Result.success(newReport)
    }

    override suspend fun login(email: String, pass: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, pass).await()
            val fbUser = authResult.user
            val uid = fbUser?.uid ?: "usr-01"
            val doc = firestore.collection("users").document(uid).get().await()
            val user = if (doc.exists()) {
                User(
                    id = uid,
                    name = doc.getString("name") ?: fbUser?.displayName ?: fbUser?.email?.substringBefore("@") ?: "Shivam Singh",
                    email = doc.getString("email") ?: fbUser?.email ?: email,
                    memberSince = doc.getString("memberSince") ?: "Active",
                    bloodGroup = doc.getString("bloodGroup") ?: "B+",
                    age = doc.getLong("age")?.toInt() ?: 35,
                    gender = doc.getString("gender") ?: "Male",
                    nickname = doc.getString("nickname") ?: "",
                    emergencyContact = doc.getString("emergencyContact") ?: "",
                    allergies = doc.getString("allergies") ?: "",
                    isProfileCompleted = doc.getBoolean("isProfileCompleted") ?: UserProfilePreferences.isProfileCompleted()
                )
            } else {
                UserProfilePreferences.loadUser(
                    User(
                        id = uid,
                        name = fbUser?.displayName ?: fbUser?.email?.substringBefore("@") ?: "Shivam Singh",
                        email = fbUser?.email ?: email,
                        memberSince = "Active",
                        bloodGroup = "B+",
                        age = 35,
                        gender = "Male",
                        isProfileCompleted = UserProfilePreferences.isProfileCompleted()
                    )
                )
            }
            UserProfilePreferences.saveUser(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(email: String, pass: String, name: String): Result<User> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, pass).await()
            val fbUser = authResult.user
            val user = User(
                id = fbUser?.uid ?: "usr-01",
                name = if (name.isNotBlank()) name else fbUser?.email?.substringBefore("@") ?: "Shivam Singh",
                email = fbUser?.email ?: email,
                memberSince = "October 2026",
                bloodGroup = "B+",
                age = 35,
                gender = "Male",
                isProfileCompleted = false
            )
            fbUser?.uid?.let { uid ->
                val userMap = hashMapOf(
                    "id" to uid,
                    "name" to user.name,
                    "nickname" to user.nickname,
                    "email" to user.email,
                    "memberSince" to user.memberSince,
                    "bloodGroup" to user.bloodGroup,
                    "age" to user.age,
                    "gender" to user.gender,
                    "emergencyContact" to user.emergencyContact,
                    "allergies" to user.allergies,
                    "isProfileCompleted" to false
                )
                firestore.collection("users").document(uid).set(userMap).await()
            }
            UserProfilePreferences.saveUser(user)
            UserProfilePreferences.setProfileCompleted(false)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserProfile(user: User): Result<User> {
        val updated = user.copy(isProfileCompleted = true)
        UserProfilePreferences.saveUser(updated)
        UserProfilePreferences.setProfileCompleted(true)
        val currentUserId = auth.currentUser?.uid ?: user.id
        return try {
            val userMap = hashMapOf(
                "id" to currentUserId,
                "name" to updated.name,
                "nickname" to updated.nickname,
                "email" to updated.email,
                "memberSince" to updated.memberSince,
                "bloodGroup" to updated.bloodGroup,
                "age" to updated.age,
                "gender" to updated.gender,
                "emergencyContact" to updated.emergencyContact,
                "allergies" to updated.allergies,
                "isProfileCompleted" to true
            )
            firestore.collection("users").document(currentUserId).set(userMap).await()
            Result.success(updated)
        } catch (_: Exception) {
            // Succeeded locally even if firestore write failed
            Result.success(updated)
        }
    }

    override fun isProfileCompleted(): Boolean {
        return UserProfilePreferences.isProfileCompleted()
    }
}
