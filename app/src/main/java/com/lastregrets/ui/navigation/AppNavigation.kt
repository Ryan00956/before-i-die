package com.lastregrets.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lastregrets.data.local.ResonateStore
import com.lastregrets.data.repository.RegretRepository
import com.lastregrets.data.repository.TodoRepository
import com.lastregrets.ui.components.OfflineBanner
import com.lastregrets.ui.screens.*
import com.lastregrets.ui.theme.*
import com.lastregrets.ui.viewmodel.*

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Splash : Screen("splash", "", Icons.Default.Home)
    data object Home : Screen("home", "首页", Icons.Default.Home)
    data object Square : Screen("square", "广场", Icons.Default.Article)
    data object Publish : Screen("publish", "发布", Icons.Default.AddCircle)
    data object Todo : Screen("todo", "待办", Icons.Default.CheckCircle)
    data object Insights : Screen("insights", "洞察", Icons.Default.BarChart)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Square,
    Screen.Publish,
    Screen.Todo,
    Screen.Insights
)

@Composable
fun AppNavigation(
    regretRepository: RegretRepository,
    todoRepository: TodoRepository,
    resonateStore: ResonateStore
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        containerColor = DeepNavy,
        bottomBar = {
            if (currentRoute != null && currentRoute != Screen.Splash.route) {
                NavigationBar(
                    containerColor = DarkBlue,
                    contentColor = TextPrimary,
                    tonalElevation = 8.dp
                ) {
                    bottomNavItems.forEach { screen ->
                        val selected = currentRoute == screen.route
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    screen.icon,
                                    contentDescription = screen.title,
                                    modifier = Modifier.size(if (screen == Screen.Publish) 28.dp else 24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = screen.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = WarmAmber,
                                selectedTextColor = WarmAmber,
                                unselectedIconColor = TextHint,
                                unselectedTextColor = TextHint,
                                indicatorColor = MidnightBlue
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route,
            ) {
                composable(Screen.Splash.route) {
                    SplashScreen(
                        onFinished = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Screen.Home.route) {
                    val viewModel: HomeViewModel = viewModel(
                        factory = HomeViewModel.factory(regretRepository, todoRepository, resonateStore)
                    )
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                    HomeScreen(
                        uiState = uiState,
                        onRefresh = viewModel::refreshDailyRegret,
                        onResonate = { viewModel.resonateWithRegret() },
                        onAddToTodo = {
                            uiState.dailyRegret?.let { viewModel.addToTodo(it) }
                        },
                        onNavigateToSquare = {
                            navController.navigate(Screen.Square.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onNavigateToPublish = {
                            navController.navigate(Screen.Publish.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onDismissToast = viewModel::dismissToast
                    )
                }

                composable(Screen.Square.route) {
                    val viewModel: RegretSquareViewModel = viewModel(
                        factory = RegretSquareViewModel.factory(regretRepository, todoRepository, resonateStore)
                    )
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                    RegretSquareScreen(
                        uiState = uiState,
                        onSelectCategory = viewModel::selectCategory,
                        onResonate = { regret -> viewModel.resonate(regret) },
                        onAddToTodo = viewModel::addToTodo,
                        onDismissToast = viewModel::dismissToast,
                        onRefresh = viewModel::refresh
                    )
                }

                composable(Screen.Publish.route) {
                    val viewModel: PublishViewModel = viewModel(
                        factory = PublishViewModel.factory(regretRepository)
                    )
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                    PublishScreen(
                        uiState = uiState,
                        onContentChange = viewModel::updateContent,
                        onCategorySelect = viewModel::selectCategory,
                        onSubmit = viewModel::submit,
                        onReset = viewModel::reset
                    )
                }

                composable(Screen.Todo.route) {
                    val viewModel: TodoViewModel = viewModel(
                        factory = TodoViewModel.factory(todoRepository)
                    )
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                    TodoScreen(
                        uiState = uiState,
                        onToggleComplete = { todo ->
                            if (todo.isCompleted) viewModel.uncompleteTodo(todo.id)
                            else viewModel.completeTodo(todo.id)
                        },
                        onDelete = viewModel::deleteTodo,
                        onToggleShowCompleted = viewModel::toggleShowCompleted
                    )
                }

                composable(Screen.Insights.route) {
                    val viewModel: InsightsViewModel = viewModel(
                        factory = InsightsViewModel.factory(regretRepository, todoRepository)
                    )
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                    InsightsScreen(uiState = uiState)
                }
            }

            // 离线横幅覆盖在顶部
            if (currentRoute != null && currentRoute != Screen.Splash.route) {
                Box(modifier = Modifier.align(Alignment.TopCenter)) {
                    OfflineBanner()
                }
            }
        }
    }
}
