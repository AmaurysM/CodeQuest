package com.amaurysdm.codequest.ui.register.parentorchild

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import com.amaurysdm.codequest.customcomposables.CreateText

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ParentOrChildView(
    navController: NavHostController = rememberNavController(),
    parentOrChildViewmodel: ParentOrChildViewmodel = viewModel(),
) {

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
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .shadow(
                        100.dp,
                        MaterialTheme.shapes.medium,
                        spotColor = Color.Black,
                        ambientColor = Color.Black
                    )
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(top = 15.dp, bottom = 15.dp, start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraSmall)
                        .padding(vertical = 10.dp)
                        .background(Color(0x88a1ba06))
                        .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_child_care_24),
                        contentDescription = null,
                        modifier = Modifier.size(150.dp)
                    )

                    Button(onClick = { parentOrChildViewmodel.goToChild(navController) },
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Text(text = "I am a child")
                    }

                }

                CreateText("OR", modifier = Modifier.padding(28.dp))

                Column(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraSmall)
                        .padding(vertical = 10.dp)
                        .background(Color(0x88a1ba06))
                        .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_face_24),
                        contentDescription = null,
                        modifier = Modifier.size(150.dp)
                    )

                    Button(onClick = { parentOrChildViewmodel.goToParent(navController) },
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Text(text = "I am a parent")
                    }
                }

            }
        }
    }
}