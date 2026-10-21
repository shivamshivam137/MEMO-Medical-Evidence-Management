package com.memo.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.memo.app.data.local.UserProfilePreferences
import com.memo.app.data.repository.FirebaseMemoRepository
import com.memo.app.data.repository.IMemoRepository
import com.memo.app.data.repository.MockMemoRepository
import com.memo.app.ui.screens.*
import com.memo.app.viewmodel.*

@Composable
fun MemoNavGraph(
    navController: NavHostController = rememberNavController(),
    repository: IMemoRepository = try {
        FirebaseMemoRepository()
    } catch (_: Throwable) {
        MockMemoRepository()
    }
) {
    val context = LocalContext.current
    UserProfilePreferences.init(context)

    // Shared ViewModels backed by repository interface
    val authViewModel = AuthViewModel(repository)
    val dashboardViewModel = DashboardViewModel(repository)
    val reportsViewModel = ReportsViewModel(repository)
    val timelineViewModel = TimelineViewModel(repository)
    val searchViewModel = SearchViewModel(repository)
    val uploadViewModel = UploadViewModel(repository)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Routes.DASHBOARD,
        Routes.REPORTS,
        Routes.TIMELINE,
        Routes.SEARCH,
        Routes.PROFILE
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                MemoBottomNav(
                    currentRoute = currentRoute,
                    onNavigate = { destination ->
                        navController.navigate(destination) {
                            popUpTo(Routes.DASHBOARD) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.SPLASH) {
                SplashScreen(
                    onTimeout = {
                        val destination = if (authViewModel.uiState.value.isLoggedIn) {
                            if (!authViewModel.isProfileCompleted()) Routes.ONBOARDING_PROFILE else Routes.DASHBOARD
                        } else {
                            Routes.LOGIN
                        }
                        navController.navigate(destination) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.LOGIN) {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = { isFirstTime ->
                        val targetRoute = if (isFirstTime) Routes.ONBOARDING_PROFILE else Routes.DASHBOARD
                        navController.navigate(targetRoute) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.ONBOARDING_PROFILE) {
                OnboardingProfileScreen(
                    authViewModel = authViewModel,
                    onProfileCompleted = {
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.ONBOARDING_PROFILE) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.DASHBOARD) {
                DashboardScreen(
                    viewModel = dashboardViewModel,
                    onNavigateToReport = { id -> navController.navigate(Routes.reportDetail(id)) },
                    onNavigateToReportsList = { navController.navigate(Routes.REPORTS) },
                    onNavigateToUpload = { navController.navigate(Routes.UPLOAD) },
                    onNavigateToProfile = { navController.navigate(Routes.PROFILE) }
                )
            }

            composable(Routes.REPORTS) {
                ReportsListScreen(
                    viewModel = reportsViewModel,
                    onNavigateToReport = { id -> navController.navigate(Routes.reportDetail(id)) }
                )
            }

            composable(Routes.TIMELINE) {
                TimelineScreen(
                    viewModel = timelineViewModel,
                    onNavigateToReport = { id -> navController.navigate(Routes.reportDetail(id)) }
                )
            }

            composable(Routes.SEARCH) {
                SearchScreen(
                    viewModel = searchViewModel,
                    onNavigateToReport = { id -> navController.navigate(Routes.reportDetail(id)) }
                )
            }

            composable(Routes.UPLOAD) {
                UploadScreen(
                    viewModel = uploadViewModel,
                    onNavigateToReport = { id ->
                        navController.navigate(Routes.reportDetail(id)) {
                            popUpTo(Routes.DASHBOARD)
                        }
                    }
                )
            }

            composable(Routes.PROFILE) {
                ProfileScreen(
                    authViewModel = authViewModel,
                    onLogout = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(
                route = Routes.REPORT_DETAIL,
                arguments = listOf(navArgument("reportId") { type = NavType.StringType })
            ) { backStackEntry ->
                val reportId = backStackEntry.arguments?.getString("reportId") ?: "rpt-001"
                ReportDetailScreen(
                    reportId = reportId,
                    viewModel = reportsViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
