package com.testziliao.app.ui

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookOpen
import androidx.compose.material.icons.outlined.FolderKanban
import androidx.compose.material.icons.outlined.Heart
import androidx.compose.material.icons.outlined.UserRound
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.testziliao.app.ui.navigation.AppRoutes
import com.testziliao.app.ui.screen.CategoryScreen
import com.testziliao.app.ui.screen.FavoritesScreen
import com.testziliao.app.ui.screen.HomeScreen
import com.testziliao.app.ui.screen.ArticleDetailScreen
import com.testziliao.app.ui.screen.ArticleListScreen
import com.testziliao.app.ui.screen.ProfileScreen
import com.testziliao.app.ui.screen.QuestionSetDetailScreen
import com.testziliao.app.ui.screen.QuestionSetListScreen
import com.testziliao.app.ui.screen.SearchScreen

@Composable
fun TestApp() {
    val navController = rememberNavController()
    val items = listOf(
        TopLevelDestination(AppRoutes.HOME, "首页", Icons.Outlined.BookOpen),
        TopLevelDestination(AppRoutes.CATEGORY, "分类", Icons.Outlined.FolderKanban),
        TopLevelDestination(AppRoutes.FAVORITES, "收藏", Icons.Outlined.Heart),
        TopLevelDestination(AppRoutes.PROFILE, "我的", Icons.Outlined.UserRound)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            androidx.compose.material3.Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoutes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppRoutes.HOME) {
                HomeScreen(
                    onCategoryClick = { categoryId, categoryName ->
                        navController.navigate(AppRoutes.articleListRoute(categoryId, categoryName))
                    },
                    onArticleClick = { articleId ->
                        navController.navigate(AppRoutes.articleDetailRoute(articleId))
                    },
                    onQuestionSetClick = { questionSetId ->
                        navController.navigate(AppRoutes.questionSetDetailRoute(questionSetId))
                    }
                )
            }
            composable(AppRoutes.CATEGORY) {
                CategoryScreen(
                    onCategoryClick = { categoryId, categoryName, type ->
                        val route = if (type == "question") {
                            AppRoutes.questionSetListRoute(categoryId, categoryName)
                        } else {
                            AppRoutes.articleListRoute(categoryId, categoryName)
                        }
                        navController.navigate(route)
                    }
                )
            }
            composable(AppRoutes.FAVORITES) {
                FavoritesScreen(
                    onArticleClick = { articleId ->
                        navController.navigate(AppRoutes.articleDetailRoute(articleId))
                    },
                    onQuestionSetClick = { questionSetId ->
                        navController.navigate(AppRoutes.questionSetDetailRoute(questionSetId))
                    }
                )
            }
            composable(AppRoutes.PROFILE) {
                ProfileScreen(
                    onArticleClick = { articleId ->
                        navController.navigate(AppRoutes.articleDetailRoute(articleId))
                    },
                    onQuestionSetClick = { questionSetId ->
                        navController.navigate(AppRoutes.questionSetDetailRoute(questionSetId))
                    }
                )
            }
            composable(AppRoutes.SEARCH) {
                SearchScreen(
                    onBackClick = { navController.popBackStack() },
                    onArticleClick = { articleId ->
                        navController.navigate(AppRoutes.articleDetailRoute(articleId))
                    },
                    onQuestionSetClick = { questionSetId ->
                        navController.navigate(AppRoutes.questionSetDetailRoute(questionSetId))
                    }
                )
            }
            composable(
                route = "${AppRoutes.ARTICLE_LIST}/{${AppRoutes.ARG_CATEGORY_ID}}/{${AppRoutes.ARG_CATEGORY_NAME}}",
                arguments = listOf(
                    navArgument(AppRoutes.ARG_CATEGORY_ID) { type = NavType.StringType },
                    navArgument(AppRoutes.ARG_CATEGORY_NAME) { type = NavType.StringType }
                )
            ) { backStackEntry ->
                ArticleListScreen(
                    categoryId = Uri.decode(
                        backStackEntry.arguments?.getString(AppRoutes.ARG_CATEGORY_ID).orEmpty()
                    ),
                    categoryName = Uri.decode(
                        backStackEntry.arguments?.getString(AppRoutes.ARG_CATEGORY_NAME).orEmpty()
                    ),
                    onArticleClick = { articleId ->
                        navController.navigate(AppRoutes.articleDetailRoute(articleId))
                    }
                )
            }
            composable(
                route = "${AppRoutes.ARTICLE_DETAIL}/{${AppRoutes.ARG_ARTICLE_ID}}",
                arguments = listOf(
                    navArgument(AppRoutes.ARG_ARTICLE_ID) { type = NavType.StringType }
                )
            ) { backStackEntry ->
                ArticleDetailScreen(
                    articleId = Uri.decode(
                        backStackEntry.arguments?.getString(AppRoutes.ARG_ARTICLE_ID).orEmpty()
                    ),
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(
                route = "${AppRoutes.QUESTION_SET_LIST}/{${AppRoutes.ARG_CATEGORY_ID}}/{${AppRoutes.ARG_CATEGORY_NAME}}",
                arguments = listOf(
                    navArgument(AppRoutes.ARG_CATEGORY_ID) { type = NavType.StringType },
                    navArgument(AppRoutes.ARG_CATEGORY_NAME) { type = NavType.StringType }
                )
            ) { backStackEntry ->
                QuestionSetListScreen(
                    categoryId = Uri.decode(
                        backStackEntry.arguments?.getString(AppRoutes.ARG_CATEGORY_ID).orEmpty()
                    ),
                    categoryName = Uri.decode(
                        backStackEntry.arguments?.getString(AppRoutes.ARG_CATEGORY_NAME).orEmpty()
                    ),
                    onQuestionSetClick = { questionSetId ->
                        navController.navigate(AppRoutes.questionSetDetailRoute(questionSetId))
                    },
                    onSearchClick = {
                        navController.navigate(AppRoutes.SEARCH)
                    }
                )
            }
            composable(
                route = "${AppRoutes.QUESTION_SET_DETAIL}/{${AppRoutes.ARG_QUESTION_SET_ID}}",
                arguments = listOf(
                    navArgument(AppRoutes.ARG_QUESTION_SET_ID) { type = NavType.StringType }
                )
            ) { backStackEntry ->
                QuestionSetDetailScreen(
                    questionSetId = Uri.decode(
                        backStackEntry.arguments?.getString(AppRoutes.ARG_QUESTION_SET_ID).orEmpty()
                    ),
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
