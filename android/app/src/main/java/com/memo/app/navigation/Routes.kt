package com.memo.app.navigation

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val REPORTS = "reports"
    const val REPORT_DETAIL = "report_detail/{reportId}"
    const val TIMELINE = "timeline"
    const val SEARCH = "search"
    const val UPLOAD = "upload"
    const val PROFILE = "profile"

    fun reportDetail(reportId: String): String = "report_detail/$reportId"
}
