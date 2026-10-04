package com.memo.app.data.model

enum class ReportType(val displayName: String) {
    ALL("All"),
    BLOOD_TEST("Blood Test"),
    URINE_TEST("Urine Test"),
    PRESCRIPTION("Prescription"),
    IMAGING("Imaging"),
    DISCHARGE_SUMMARY("Discharge")
}

enum class ParameterStatus {
    NORMAL,
    ELEVATED,
    HIGH,
    LOW,
    ABNORMAL
}

data class LabResult(
    val testName: String,
    val value: String,
    val unit: String,
    val referenceRange: String,
    val status: ParameterStatus = ParameterStatus.NORMAL
)

data class Medication(
    val name: String,
    val dosage: String,
    val frequency: String,
    val duration: String,
    val instructions: String = ""
)

data class MedicalReport(
    val id: String,
    val title: String,
    val type: ReportType,
    val date: String,             // Format: YYYY-MM-DD
    val organization: String,
    val doctor: String,
    val summary: String,
    val hasAbnormalValues: Boolean = false,
    val labResults: List<LabResult> = emptyList(),
    val medications: List<Medication> = emptyList(),
    val findings: String = "",
    val originalFileName: String = ""
)

data class User(
    val id: String,
    val name: String,
    val email: String,
    val memberSince: String,
    val bloodGroup: String = "B+",
    val age: Int = 42,
    val gender: String = "Male"
)

data class HealthStats(
    val totalReports: Int,
    val bloodTestsCount: Int,
    val urineTestsCount: Int,
    val imagingCount: Int,
    val prescriptionsCount: Int,
    val attentionRequiredCount: Int
)
