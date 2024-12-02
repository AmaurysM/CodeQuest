package com.amaurysdm.codequest

import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.amaurysdm.codequest.navigation.CodeQuestNavHost
import com.amaurysdm.codequest.ui.theme.CodeQuestTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Makes the app stay in portrait mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContent {

            CodeQuestApp()

        }
    }
}

@Composable
fun CodeQuestApp() {
    val context = LocalContext.current
    val soundManager = MediaPlayer.create(context, R.raw.outdoors)
    DisposableEffect(Unit) {
        soundManager.isLooping = true
        soundManager.start()
        onDispose {
            soundManager.release()
        }
    }
    CodeQuestTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            CodeQuestNavHost(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CodeQuestApp()
}