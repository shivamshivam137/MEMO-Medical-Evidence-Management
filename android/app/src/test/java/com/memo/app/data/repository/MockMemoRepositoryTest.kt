package com.memo.app.data.repository

import com.memo.app.data.model.ReportType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MockMemoRepositoryTest {

    private lateinit var repository: MockMemoRepository

    @Before
    fun setup() {
        repository = MockMemoRepository()
    }

    @Test
    fun testRepositoryInitialization_hasReports() = runTest {
        val reports = repository.getReports().first()
        assertTrue("Reports list should not be empty", reports.isNotEmpty())
        // DemoDataSource is a singleton; other tests may add records via uploadReport.
        // We assert >= 12 to remain stable regardless of test execution order.
        assertTrue("Initial dataset should have at least 12 records", reports.size >= 12)
    }

    @Test
    fun testFilteringByBloodTest() = runTest {
        val bloodReports = repository.getFilteredReports(ReportType.BLOOD_TEST, "").first()
        assertTrue("Should return blood test reports", bloodReports.isNotEmpty())
        assertTrue("All filtered reports should be BLOOD_TEST", bloodReports.all { it.type == ReportType.BLOOD_TEST })
    }

    @Test
    fun testSearchByQuery_findsMatches() = runTest {
        val queryResults = repository.getFilteredReports(ReportType.ALL, "HbA1c").first()
        assertTrue("Query for 'HbA1c' should return matching reports", queryResults.isNotEmpty())
    }

    @Test
    fun testHealthStatsCalculation() = runTest {
        val stats = repository.getHealthStats().first()
        assertTrue("Total reports should match count", stats.totalReports >= 12)
        assertTrue("Blood tests count should be greater than 0", stats.bloodTestsCount > 0)
    }

    @Test
    fun testUploadReport_appendsRecord() = runTest {
        val initialCount = repository.getReports().first().size
        val result = repository.uploadReport("new_test_record.pdf")
        assertTrue("Upload should succeed", result.isSuccess)

        val updatedReports = repository.getReports().first()
        assertEquals("Reports size should increment by 1", initialCount + 1, updatedReports.size)
    }
}
