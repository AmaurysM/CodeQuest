package com.amaurysdm.codequest.ui.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.amaurysdm.codequest.R
import kotlinx.coroutines.launch

@Composable
fun RegisterView(
    navController: NavHostController = rememberNavController(),
    registerViewmodel: RegisterViewmodel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    var selectedOption by remember { mutableStateOf<String>("") }

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
            Column(
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
                    .padding(top = 5.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Child Option
                    SelectionCard(
                        modifier = Modifier.weight(0.9f),
                        title = "I'm a Child",
                        subtitle = "Start learning",
                        icon = R.drawable.baseline_child_care_24,
                        isSelected = selectedOption == "child",
                        gradientColors = listOf(
                            Color(0xFF4CAF50),
                            Color(0xFF8BC34A)
                        ),
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            selectedOption = "child"
                            registerViewmodel.registerData =
                                registerViewmodel.registerData.copy(areYouAParent = false)
                        }
                    )

                    SelectionCard(
                        modifier = Modifier.weight(1f),
                        title = "I'm a Parent",
                        subtitle = "Monitor progress",
                        icon = R.drawable.baseline_face_24,
                        isSelected = selectedOption == "parent",
                        gradientColors = listOf(
                            Color(0xFF2196F3),
                            Color(0xFF03DAC5)
                        ),
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            selectedOption = "parent"
                            registerViewmodel.registerData =
                                registerViewmodel.registerData.copy(areYouAParent = true)
                        }
                    )
                }

                OutlinedTextField(
                    value = registerViewmodel.registerData.username,
                    onValueChange = {
                        registerViewmodel.registerData =
                            registerViewmodel.registerData.copy(username = it)
                    },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = registerViewmodel.registerData.email,
                    onValueChange = {
                        registerViewmodel.registerData =
                            registerViewmodel.registerData.copy(email = it)
                    },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = registerViewmodel.registerData.password,
                    onValueChange = {
                        registerViewmodel.registerData =
                            registerViewmodel.registerData.copy(password = it)
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            registerViewmodel.togglePasswordVisibility()
                        }) {
                            Icon(
                                painter = painterResource(id = registerViewmodel.iconPassword()),
                                contentDescription = "Visibility"
                            )
                        }
                    },
                    visualTransformation = if (registerViewmodel.passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = registerViewmodel.registerData.confirmPassword,
                    onValueChange = {
                        registerViewmodel.registerData =
                            registerViewmodel.registerData.copy(confirmPassword = it)
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            registerViewmodel.toggleConfirmPasswordVisibility()
                        }) {
                            Icon(
                                painter = painterResource(id = registerViewmodel.iconConfirmPassword()),
                                contentDescription = "Visibility"
                            )
                        }
                    },
                    visualTransformation = if (registerViewmodel.confirmPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    label = { Text("Confirm Password") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                registerViewmodel.goBack(navController)
                            }
                        },
                        shape = MaterialTheme.shapes.small,

                        ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                            contentDescription = "Back Button"
                        )
                    }

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                registerViewmodel.register(navController)
                            }

                        }, shape = MaterialTheme.shapes.small, modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Register")
                    }
                }

                Row(
                    modifier = Modifier.clickable {
                        registerViewmodel.goToLogin(navController)
                    }

                ) {
                    Text(text = "Already have an account? ")
                    Text(text = "Login", color = MaterialTheme.colorScheme.primary)
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    RegisterView()
}

@Composable
private fun SelectionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: Int,
    isSelected: Boolean,
    gradientColors: List<Color>,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(
                3.dp,
                Brush.linearGradient(gradientColors)
            )
        } else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = if (isSelected) gradientColors else listOf(
                                Color.Gray.copy(alpha = 0.3f),
                                Color.Gray.copy(alpha = 0.2f)
                            )
                        ),
                        shape = RoundedCornerShape(32.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = if (isSelected) Color.White else Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(width = 2.dp))
            Column() {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) gradientColors[0] else Color.Black
                    ),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isSelected) gradientColors[0].copy(alpha = 0.8f) else Color.Gray
                    ),
                    textAlign = TextAlign.Center
                )
            }

        }


    }
}