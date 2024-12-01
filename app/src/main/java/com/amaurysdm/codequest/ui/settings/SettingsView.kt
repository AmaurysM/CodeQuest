package com.amaurysdm.codequest.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SettingsView(
    navController: NavHostController = rememberNavController(),
    settingsViewmodel: SettingsViewmodel = viewModel()
) {

    val kids by settingsViewmodel.childrenFlow.collectAsState(emptyList())
    val kidLevels by settingsViewmodel.editingChildLevelsFlow.collectAsState(emptyList())

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
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(40.dp)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.surface)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
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

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = "Username: ${settingsViewmodel.user.username}")
                Text(text = "Email: ${settingsViewmodel.user.email}")
            }

            if (settingsViewmodel.dropDownActive) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .verticalScroll(state = rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column {
                        kids.forEach {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = it.email)
                                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.baseline_edit_24),
                                    contentDescription = null,
                                    modifier = Modifier.clickable {
                                        settingsViewmodel.isEditingChild = true
                                        settingsViewmodel.observeChildLevels(it)

                                    })

                            }
                        }
                    }

                    Button(
                        onClick = { settingsViewmodel.isAddingChild = true },
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Text(text = "Add Child")
                    }

                }
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { settingsViewmodel.back(navController) },
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(text = "Back")
                }

                if (settingsViewmodel.isParent) {
                    Icon(imageVector =
                    if (!settingsViewmodel.dropDownActive)
                        Icons.Filled.KeyboardArrowDown
                    else
                        Icons.Filled.KeyboardArrowUp,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                settingsViewmodel.dropDownActive = !settingsViewmodel.dropDownActive
                            }
                            .size(30.dp)
                    )
                }

                Button(
                    onClick = { settingsViewmodel.logout(navController) },
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(text = "Logout")
                }
            }
        }

        if (settingsViewmodel.isAddingChild) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable {
                        ; // Do nothing
                    }
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(40.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "Add Child")


                    Column {
                        OutlinedTextField(
                            value = settingsViewmodel.newChild.email,
                            onValueChange = {
                                settingsViewmodel.newChild =
                                    settingsViewmodel.newChild.copy(email = it)
                            },
                            label = { Text(text = "Child Email") }
                        )
                    }

                    Row {
                        Button(
                            onClick = {
                                //settingsViewmodel.isAddingChild.value = !settingsViewmodel.isAddingChild.value
                                settingsViewmodel.isAddingChild = false
                                //settingsViewmodel.addChild()
                            }
                        ) {
                            Text(text = "Cancel")
                        }

                        Button(onClick = {
                            settingsViewmodel.isAddingChild = false
                            settingsViewmodel.addChild()

                            //scope.launch { settingsViewmodel.addChild() }
                        }
                        ) {
                            Text(text = "Add")
                        }
                    }


                }

            }

        }

        if (settingsViewmodel.isEditingChild) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable {
                        ; // Do nothing
                    }
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(40.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "Child Info")


                    Column {
                        Text(text = "Child Email: ${settingsViewmodel.editingChild.email}")
                        Text(text = "Child Username: ${settingsViewmodel.editingChild.username}")
                        Text(text = "Completed Levels: ${kidLevels.size}")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                //settingsViewmodel.isEditingChild.value = !settingsViewmodel.isEditingChild.value
                                settingsViewmodel.isEditingChild = false

                            }, shape = MaterialTheme.shapes.extraSmall
                        ) {
                            Text(text = "Cancel")
                        }

                        Button(
                            onClick = {
                                settingsViewmodel.isEditingChild = false
                                settingsViewmodel.removeChild()
                                //scope.launch { settingsViewmodel.removeChild() }
                            }, shape = MaterialTheme.shapes.extraSmall
                        ) {
                            Text(text = "Remove")
                        }
                    }


                }

            }
        }

    }
}