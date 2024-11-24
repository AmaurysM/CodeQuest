package com.amaurysdm.codequest.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.amaurysdm.codequest.ui.home.HomeView
import com.amaurysdm.codequest.ui.level.LevelView
import com.amaurysdm.codequest.ui.levelselect.LevelSelectView
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
        startDestination = Screens.Splash.route
    ){

        composable(route = Screens.Splash.route){
            SplashView(navController = navController)
        }

        navigation( startDestination = Screens.UserCreationChild.Welcome.route
            , route = Screens.UserCreation.route
        ){
            composable(route = Screens.UserCreationChild.Welcome.route){
                WelcomeView(navController = navController)
            }

            composable(route = Screens.UserCreationChild.Login.route){
                LoginView(navController = navController)
            }

            composable(route = Screens.UserCreationChild.Register.route){
                RegisterView(navController = navController)
            }

        }

        navigation( startDestination = Screens.GeneralChild.Home.route
        , route = Screens.General.route
        ){
            composable(route = Screens.GeneralChild.Home.route){
                HomeView(navController = navController)
            }

            composable(route = Screens.GeneralChild.Profile.route){
            }

            composable(route = Screens.GeneralChild.Settings.route){
            }

            navigation( startDestination = Screens.GameChild.LevelSelect.route
            , route = Screens.GameChild.Game.route
            ) {
                composable(route = Screens.GameChild.LevelSelect.route) {
                    LevelSelectView(modifier = modifier, navController = navController)
                }

                composable(route = Screens.GameChild.Level.route) {
                    LevelView(modifier = modifier, navController = navController)
                }
            }
        }

    }
}