package com.amaurysdm.codequest.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.amaurysdm.codequest.R

@Preview(showBackground = true)
@Composable
fun SettingsView(
    navController: NavHostController = rememberNavController(),
    settingsViewmodel: SettingsViewmodel = viewModel()
){

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            bitmap = ImageBitmap.imageResource(id = R.drawable.grass_background),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.Center,
            contentScale = ContentScale.FillHeight
        )

        Column(
            modifier = Modifier.align(Alignment.Center)
                .fillMaxWidth()
                .padding(40.dp)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.surface)
                .padding(20.dp)
            , horizontalAlignment = Alignment.CenterHorizontally
            , verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .size(100.dp)
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_person_24),
                    contentDescription = "Back Button",
                    modifier = Modifier.fillMaxSize()

                )
            }

            Text(text = "Email: ${settingsViewmodel.userEmail}")
            Text(text = "Password: ${settingsViewmodel.userUid}")

            Button(onClick = { settingsViewmodel.logout(navController) }) {
                Text(text = "Logout")
            }

        }

    }
}