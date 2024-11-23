package com.amaurysdm.codequest.ui.splash

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.amaurysdm.codequest.navigation.CodeQuestScreens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun SplashView(
    navController: NavHostController = rememberNavController(),
    splashViewModel: SplashViewmodel = viewModel()
) {
    if (!LocalInspectionMode.current) {
        LaunchedEffect(Unit) {
            splashViewModel.navigateIfLogged(navController)
        }
    }

    val transient = rememberInfiniteTransition(label = "bobbing logo")

    val bobbing = transient.animateFloat(
        initialValue = 0f,
        targetValue = 16f,
        label = "bobbing logo",
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                -15f at 0 with FastOutLinearInEasing
                15f at 500 with LinearOutSlowInEasing
                -15f at 1000 with FastOutLinearInEasing
            }, repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "CodeQuest",
            modifier = Modifier.offset(y = bobbing.value.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold
        )
    }
}