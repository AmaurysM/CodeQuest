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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
@Composable
fun SettingsView(
    navController: NavHostController = rememberNavController(),
    settingsViewmodel: SettingsViewmodel = viewModel()
) {
    val kids by settingsViewmodel.childrenFlow.collectAsState(emptyList())
    val kidLevels by settingsViewmodel.editingChildLevelsFlow.collectAsState(emptyList())
    val myLevels by settingsViewmodel.levelsFlow.collectAsState(emptyList())

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            bitmap = ImageBitmap.imageResource(id = R.drawable.grass_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // Avatar
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_person_24),
                contentDescription = null,
                modifier = Modifier
                    .size(96.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(16.dp)
            )

            // User Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Username: ${settingsViewmodel.user.username}")
                    Text("Email: ${settingsViewmodel.user.email}")
                    Text("Levels Completed: ${myLevels.size}")
                }
            }

            // Dropdown for child accounts
            if (settingsViewmodel.dropDownActive) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        kids.forEach {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(it.email)
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_edit_24),
                                    contentDescription = "Edit Child",
                                    modifier = Modifier
                                        .clickable {
                                            settingsViewmodel.isEditingChild = true
                                            settingsViewmodel.observeChildLevels(it)
                                        }
                                        .size(24.dp)
                                )
                            }
                        }

                        Button(
                            onClick = { settingsViewmodel.isAddingChild = true },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Add Child")
                        }
                    }
                }
            }

            // Navigation and logout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { settingsViewmodel.back(navController) }) {
                    Text("Back")
                }

                if (settingsViewmodel.isParent) {
                    Icon(
                        imageVector = if (settingsViewmodel.dropDownActive) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                settingsViewmodel.dropDownActive = !settingsViewmodel.dropDownActive
                            }
                            .size(36.dp)
                    )
                }

                Button(onClick = { settingsViewmodel.logout(navController) }) {
                    Text("Logout")
                }
            }
        }

        // Add Child Dialog
        if (settingsViewmodel.isAddingChild) {
            DialogOverlay {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Add Child", style = MaterialTheme.typography.titleMedium)

                    OutlinedTextField(
                        value = settingsViewmodel.newChild.email,
                        onValueChange = {
                            settingsViewmodel.newChild = settingsViewmodel.newChild.copy(email = it)
                        },
                        label = { Text("Child Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = { settingsViewmodel.isAddingChild = false }) {
                            Text("Close")
                        }
                        Button(onClick = { settingsViewmodel.addChild() }) {
                            Text("Add")
                        }
                    }
                }
            }
        }

        // Edit Child Dialog
        if (settingsViewmodel.isEditingChild) {
            DialogOverlay {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Child Info", style = MaterialTheme.typography.titleMedium)
                    Text("Email: ${settingsViewmodel.editingChild.email}")
                    Text("Username: ${settingsViewmodel.editingChild.username}")
                    Text("Completed Levels: ${kidLevels.size}")

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = { settingsViewmodel.isEditingChild = false }) {
                            Text("Cancel")
                        }
                        Button(onClick = {
                            settingsViewmodel.isEditingChild = false
                            settingsViewmodel.removeChild()
                        }) {
                            Text("Remove")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DialogOverlay(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable(enabled = false) { }
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(modifier = Modifier.padding(24.dp)) {
                content()
            }
        }
    }
}
