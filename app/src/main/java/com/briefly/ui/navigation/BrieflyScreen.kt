package com.briefly.ui.navigation

import kotlinx.serialization.Serializable

sealed class BrieflyScreen(): Route{
    @Serializable
    data object SplashScreen : BrieflyScreen()
    @Serializable
    data object SignUpScreen : BrieflyScreen()

    @Serializable
    data object LoginScreen : BrieflyScreen()

    @Serializable
    data object HomeScreen : BrieflyScreen()

    @Serializable
    data object ArticleScreen : BrieflyScreen()

    @Serializable
    data object SearchScreen : BrieflyScreen()

    @Serializable
    data object SavedScreen : BrieflyScreen()

    @Serializable
    data object TrendingScreen : BrieflyScreen()

    @Serializable
    data object ProfileScreen : BrieflyScreen()
}