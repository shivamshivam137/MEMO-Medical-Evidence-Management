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
}
