package com.amaurysdm.codequest.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.amaurysdm.codequest.model.AuthManager
import com.amaurysdm.codequest.model.AuthState
import com.amaurysdm.codequest.ui.home.HomeView
import com.amaurysdm.codequest.ui.login.LoginView
import com.amaurysdm.codequest.ui.register.RegisterView
import com.amaurysdm.codequest.ui.splash.SplashView
import com.amaurysdm.codequest.ui.welcome.WelcomeView


@Composable
fun CodeQuestNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier.fillMaxSize()
){
    NavHost(
        navController = navController,
        startDestination = CodeQuestScreens.Splash.route
    ){

        composable(route = CodeQuestScreens.Splash.route){
            SplashView(navController = navController)
        }

        navigation( startDestination = CodeQuestScreens.Welcome.route
            , route = CodeQuestScreens.UserCreation.route
        ){
            composable(route = CodeQuestScreens.Welcome.route){
                WelcomeView(navController = navController)
            }

            composable(route = CodeQuestScreens.Login.route){
                LoginView(navController = navController)
            }

            composable(route = CodeQuestScreens.Register.route){
                RegisterView(navController = navController)
            }

        }

        navigation( startDestination = CodeQuestScreens.Home.route
        , route = CodeQuestScreens.General.route
        ){
            composable(route = CodeQuestScreens.Home.route){
                HomeView(navController = navController)
            }

            composable(route = CodeQuestScreens.Profile.route){
            }

            composable(route = CodeQuestScreens.Settings.route){
            }

            composable(route = CodeQuestScreens.Game.route){
            }

            composable(route = CodeQuestScreens.LevelSelect.route){
            }
        }

    }
}