package com.briefly.ui.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.briefly.BrieflyScreens
import com.briefly.core.viewmodels.SharedViewModel
import com.briefly.domain.model.Article
import com.briefly.ui.screen.article.ArticleScreen
import com.briefly.ui.screen.bookmark.BookMarkScreen
import com.briefly.ui.screen.home.HomeScreen
import com.briefly.ui.screen.home.HomeViewModel
import com.briefly.ui.screen.login.LoginScreen
import com.briefly.ui.screen.profile.ProfileScreen
import com.briefly.ui.screen.profile.ProfileViewModel
import com.briefly.ui.screen.search.SearchScreen
import com.briefly.ui.screen.signup.SignUpScreen
import com.briefly.ui.screen.signup.SignUpUiState
import com.briefly.ui.screen.splashscreen.BrieflySplashScreen
import com.briefly.ui.screen.trending.TrendingScreen

@Composable
fun BrieflyNavigation(navController : NavHostController){

    val sharedViewModel: SharedViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = BrieflyScreen.SplashScreen){
        composable<BrieflyScreen.SplashScreen> {
            BrieflySplashScreen(
                onNavigateToAuth = {navController.navigate(BrieflyScreen.SignUpScreen){
                    popUpTo<BrieflyScreen.SplashScreen> { inclusive = true }
                }
                }
            )
        }

        composable<BrieflyScreen.SignUpScreen> {
            SignUpScreen(
                onNavigateToSignIn = {navController.navigate(BrieflyScreen.LoginScreen){
                    popUpTo<BrieflyScreen.SignUpScreen> { inclusive = true }
                } },
                onCreateAccount = {navController.navigate(BrieflyScreen.HomeScreen)},
                onGoogleSignIn = {},
                onFacebookSignIn = {},
                navController = rememberNavController()
            )
        }

        composable<BrieflyScreen.LoginScreen>{
            LoginScreen(
                onNavigateToHome = {navController.navigate(BrieflyScreen.HomeScreen)},
                onNavigateToForgotPassword = {},
                onClose = {navController.navigate(BrieflyScreen.SplashScreen)},
                onSignUpClick = { navController.navigate(BrieflyScreen.SignUpScreen) {
                    popUpTo<BrieflyScreen.LoginScreen> { inclusive = true }
                } }
            )
        }

        composable<BrieflyScreen.HomeScreen> {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(uiState.articles) {
                if (uiState.articles.isNotEmpty()) {
                    sharedViewModel.setAllArticles(uiState.articles)
                }
            }
            HomeScreen(
                onArticleClick = { article ->
                    sharedViewModel.selectArticle(article)
                    navController.navigate(BrieflyScreen.ArticleScreen)
                },
                onNavigate = { route ->
                    when (route) {
                        "search"   -> navController.navigate(BrieflyScreen.SearchScreen)
                        "saved"    -> navController.navigate(BrieflyScreen.SavedScreen)
                        "trending" -> navController.navigate(BrieflyScreen.TrendingScreen)
                        "profile"  -> navController.navigate(BrieflyScreen.ProfileScreen)
                    }
                }
            )
        }

        composable<BrieflyScreen.ArticleScreen> {
            val article = sharedViewModel.selectedArticle.collectAsStateWithLifecycle()
            val allArticles = sharedViewModel.allArticles.collectAsStateWithLifecycle()
            val bookmarkedArticles by sharedViewModel.bookmarkedArticles.collectAsStateWithLifecycle()

            article.value?.let { selectedArticle ->
                val related = allArticles.value
                    .filter { it.id != selectedArticle.id }  // exclude current
                    .shuffled()
                    .take(5)                                   // take 5 random


                ArticleScreen(
                    article = selectedArticle,
                    isBookmarked = bookmarkedArticles.any { it.id == selectedArticle.id },
                    onBookmarkToggle = { sharedViewModel.toggleBookmark(selectedArticle) },
                    relatedArticle = related,
                    onBack = { navController.popBackStack() },
                    onArticleClick = { related ->
                        sharedViewModel.selectArticle(related)
                        navController.navigate(BrieflyScreen.ArticleScreen)
                    },
                    onCategoryNavigate = {
                        navController.navigate(BrieflyScreen.HomeScreen) {
                            popUpTo<BrieflyScreen.HomeScreen> { inclusive = true }
                        }
                    }
                )
            }

        }

        composable<BrieflyScreen.SearchScreen>{
            SearchScreen(
                sharedViewModel = sharedViewModel,
                onNavigate = { route ->
                    when (route) {
                        "home"   -> navController.navigate(BrieflyScreen.HomeScreen)
                        "saved"    -> navController.navigate(BrieflyScreen.SavedScreen)
                        "trending" -> navController.navigate(BrieflyScreen.TrendingScreen)
                        "profile"  -> navController.navigate(BrieflyScreen.ProfileScreen)
                    }
                },
                onArticleClick = { article ->
                    sharedViewModel.selectArticle(article)
                    navController.navigate(
                        BrieflyScreen.ArticleScreen
                    )
                }
            )
        }

        composable<BrieflyScreen.SavedScreen>{
            BookMarkScreen(
                sharedViewModel = sharedViewModel,  // ← pass it
                onArticleClick = {
                    navController.navigate(
                        BrieflyScreen.ArticleScreen
                    )
                },
                onNavigate = { route ->
                    when (route) {
                        "home"      -> navController.navigate(BrieflyScreen.HomeScreen)
                        "search"    -> navController.navigate(BrieflyScreen.SearchScreen)
                        "trending"  -> navController.navigate(BrieflyScreen.TrendingScreen)
                        "profile"   -> navController.navigate(BrieflyScreen.ProfileScreen)
                    }
                },
                onShareClick = {},
                onUnSaveClick = {}
            )
        }

        composable<BrieflyScreen.TrendingScreen>{
            TrendingScreen(
                onArticleClick = {
                    navController.navigate(
                        BrieflyScreen.ArticleScreen
                    )
                },
                onNavigate = { route ->
                    when (route) {
                        "home"      -> navController.navigate(BrieflyScreen.HomeScreen)
                        "search"    -> navController.navigate(BrieflyScreen.SearchScreen)
                        "saved"     -> navController.navigate(BrieflyScreen.SavedScreen)
                        "profile"   -> navController.navigate(BrieflyScreen.ProfileScreen)
                    }
                }
            )
        }

        composable<BrieflyScreen.ProfileScreen>{
            val profileViewModel: ProfileViewModel = hiltViewModel()
            val allArticles by sharedViewModel.allArticles.collectAsStateWithLifecycle()
            val bookmarkedArticles by sharedViewModel.bookmarkedArticles.collectAsStateWithLifecycle()

            // Sync real stats whenever articles change
            LaunchedEffect(allArticles, bookmarkedArticles) {
                profileViewModel.updateStatsFromArticles(bookmarkedArticles, allArticles)
            }

            ProfileScreen(
                viewModel = profileViewModel,
                onNavigate = { route ->
                    when (route) {
                        "home"     -> navController.navigate(BrieflyScreen.HomeScreen)
                        "search"   -> navController.navigate(BrieflyScreen.SearchScreen)
                        "saved"    -> navController.navigate(BrieflyScreen.SavedScreen)
                        "trending" -> navController.navigate(BrieflyScreen.TrendingScreen)
                    }
                },
                onSignOut = {
                    navController.navigate(BrieflyScreen.LoginScreen) {
                        popUpTo<BrieflyScreen.HomeScreen> { inclusive = true }
                    }
                }
            )
        }
    }

}