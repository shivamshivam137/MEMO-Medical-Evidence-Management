package com.memo.app.data.repository

import com.memo.app.data.mock.DemoDataSource
import com.memo.app.data.model.HealthStats
import com.memo.app.data.model.MedicalReport
import com.memo.app.data.model.ReportType
import com.memo.app.data.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class MockMemoRepository(
    private val dataSource: DemoDataSource = DemoDataSource
) : IMemoRepository {

    private val _reportsFlow = MutableStateFlow<List<MedicalReport>>(dataSource.reports.toList())
    val reportsFlow = _reportsFlow.asStateFlow()

    private val _userFlow = MutableStateFlow<User>(dataSource.currentUser)

    override fun getCurrentUser(): Flow<User> = _userFlow

    override fun getReports(): Flow<List<MedicalReport>> = _reportsFlow

    override fun getReportById(id: String): Flow<MedicalReport?> {
        return _reportsFlow.map { list -> list.find { it.id == id } }
    }

    override fun getHealthStats(): Flow<HealthStats> {
        return _reportsFlow.map { list ->
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
        return _reportsFlow.map { list ->
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
        // Simulated OCR Processing delay: 2.5 seconds total
        delay(2500)
        val newReport = dataSource.addUploadedReport(fileName)
        _reportsFlow.value = dataSource.reports.toList()
        return Result.success(newReport)
    }

    override suspend fun login(email: String, pass: String): Result<User> {
        delay(600)
        return if (email.isNotBlank() && pass.isNotBlank()) {
            Result.success(dataSource.currentUser)
        } else {
            Result.failure(IllegalArgumentException("Please enter a valid email and password"))
        }
    }
}
