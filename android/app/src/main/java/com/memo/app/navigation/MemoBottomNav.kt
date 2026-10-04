package com.memo.app.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.memo.app.ui.theme.Slate200
import com.memo.app.ui.theme.Slate500
import com.memo.app.ui.theme.SurfaceWhite
import com.memo.app.ui.theme.Teal600

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Dashboard : BottomNavItem(Routes.DASHBOARD, "Overview", Icons.Filled.Dashboard, Icons.Outlined.Dashboard)
    object Reports : BottomNavItem(Routes.REPORTS, "Reports", Icons.Filled.Description, Icons.Outlined.Description)
    object Timeline : BottomNavItem(Routes.TIMELINE, "Timeline", Icons.Filled.Timeline, Icons.Outlined.Timeline)
    object Search : BottomNavItem(Routes.SEARCH, "Search", Icons.Filled.Search, Icons.Outlined.Search)
    object Profile : BottomNavItem(Routes.PROFILE, "Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun MemoBottomNav(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Reports,
        BottomNavItem.Timeline,
        BottomNavItem.Search,
        BottomNavItem.Profile
    )

    NavigationBar(
        modifier = modifier,
        containerColor = SurfaceWhite,
        tonalElevation = 4.dp
    ) {
        items.forEach { item ->
            val isSelected = (currentRoute == item.route)
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        onNavigate(item.route)
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Teal600,
                    selectedTextColor = Teal600,
                    indicatorColor = SurfaceWhite,
                    unselectedIconColor = Slate500,
                    unselectedTextColor = Slate500
                )
            )
        }
    }
}
