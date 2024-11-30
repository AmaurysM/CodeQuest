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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.amaurysdm.codequest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun SettingsView(
    navController: NavHostController = rememberNavController(),
    settingsViewmodel: SettingsViewmodel = viewModel()
) {

    //val isAddingChild = remember { mutableStateOf(false)}
    val scope = CoroutineScope(Dispatchers.IO)
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

        Column (
            modifier = Modifier
                .align(Alignment.Center)
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

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(text = "Username: ${settingsViewmodel.user.username}")
                Text(text = "Email: ${settingsViewmodel.user.email}")
            }

            if(settingsViewmodel.areYouAParent()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        settingsViewmodel.children.forEach {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(10.dp)
                                , horizontalArrangement = Arrangement.SpaceBetween
                                , verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = it.username)
                                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.baseline_edit_24)
                                    , contentDescription = null
                                    , modifier = Modifier.clickable {
                                        settingsViewmodel.isEditingChild.value = true
                                        settingsViewmodel.editingChild = it
                                    })
                            }

                        }
                    }
                    Button(onClick = {
                            settingsViewmodel.isAddingChild.value = !settingsViewmodel.isAddingChild.value
                        }
                        , modifier = Modifier.fillMaxWidth()
                        , shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Text(text = "Add Child")
                    }

                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Button(onClick = { settingsViewmodel.back(navController) }
                    , shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(text = "Back")
                }

                Button(onClick = { settingsViewmodel.logout(navController) }
                    , shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(text = "Logout")
                }
            }
        }

        if(settingsViewmodel.isAddingChild.value) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable{
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
                        .padding(20.dp)
                    , horizontalAlignment = Alignment.CenterHorizontally
                    , verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "Add Child")


                    Column {
                        OutlinedTextField(
                            value = settingsViewmodel.newChild.email,
                            onValueChange = {settingsViewmodel.newChild = settingsViewmodel.newChild.copy(email = it)},
                            label = { Text(text = "Child Email") }
                        )
                    }

                    Row {
                        Button(
                            onClick = {
                                settingsViewmodel.isAddingChild.value = !settingsViewmodel.isAddingChild.value
                            }
                        ) {
                            Text(text = "Cancel")
                        }

                        Button(onClick = {
                            scope.launch { settingsViewmodel.addChild() }
                        }
                        ) {
                            Text(text = "Add")
                        }
                    }


                }

            }

        }

        if(settingsViewmodel.isEditingChild.value) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable{
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
                        .padding(20.dp)
                    , horizontalAlignment = Alignment.CenterHorizontally
                    , verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "Child Info")


                    Column {
                        Text(text = "Child Email: ${settingsViewmodel.editingChild.email}")
                        Text(text = "Child Username: ${settingsViewmodel.editingChild.username}")
                        Text(text = "Completed Levels: ${settingsViewmodel.editingChild.listLevels.size}")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth()
                        , horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                settingsViewmodel.isEditingChild.value = !settingsViewmodel.isEditingChild.value
                            }
                            , shape = MaterialTheme.shapes.extraSmall
                        ) {
                            Text(text = "Cancel")
                        }

                        Button(
                            onClick = {
                                scope.launch { settingsViewmodel.removeChild() }
                            }
                            , shape = MaterialTheme.shapes.extraSmall
                        ) {
                            Text(text = "Remove")
                        }
                    }


                }

            }
        }

    }
}