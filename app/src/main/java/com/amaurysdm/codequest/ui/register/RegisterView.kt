package com.amaurysdm.codequest.ui.register

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.amaurysdm.codequest.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterView(
    navController: NavHostController = rememberNavController(),
    registerViewmodel: RegisterViewmodel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    var selectedOption by remember { mutableStateOf<String>("") }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background with blur effect
        Image(
            bitmap = ImageBitmap.imageResource(id = R.drawable.grass_background),
            contentDescription = "background",
            modifier = Modifier
                .fillMaxSize()
                .blur(3.dp),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop
        )

        // Dark overlay for better readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

                // App Title
                Text(
                    text = "CodeQuest",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    ),
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )



                Text(
                    text = "Join the adventure and start coding!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 32.dp)
                )



                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 24.dp,
                            shape = RoundedCornerShape(24.dp)
                        ),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White.copy(alpha = 0.95f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {

                        // User type selection header
                        Text(
                            text = "I am a...",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // User type selection cards
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            EnhancedSelectionCard(
                                modifier = Modifier.weight(1f),
                                title = "Child",
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

                            EnhancedSelectionCard(
                                modifier = Modifier.weight(1f),
                                title = "Parent",
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

                        Spacer(modifier = Modifier.height(8.dp))

                        // Form fields
                        EnhancedTextField(
                            value = registerViewmodel.registerData.username,
                            onValueChange = {
                                registerViewmodel.registerData =
                                    registerViewmodel.registerData.copy(username = it)
                            },
                            label = "Username",
                            leadingIcon = R.drawable.baseline_person_24
                        )

                        EnhancedTextField(
                            value = registerViewmodel.registerData.email,
                            onValueChange = {
                                registerViewmodel.registerData =
                                    registerViewmodel.registerData.copy(email = it)
                            },
                            label = "Email",
                            leadingIcon = R.drawable.outline_alternate_email_24
                        )

                        EnhancedTextField(
                            value = registerViewmodel.registerData.password,
                            onValueChange = {
                                registerViewmodel.registerData =
                                    registerViewmodel.registerData.copy(password = it)
                            },
                            label = "Password",
                            leadingIcon = R.drawable.outline_lock_24,
                            trailingIcon = {
                                IconButton(onClick = {
                                    registerViewmodel.togglePasswordVisibility()
                                }) {
                                    Icon(
                                        painter = painterResource(id = registerViewmodel.iconPassword()),
                                        contentDescription = "Toggle password visibility",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            },
                            visualTransformation = if (registerViewmodel.passwordVisible) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )

                        EnhancedTextField(
                            value = registerViewmodel.registerData.confirmPassword,
                            onValueChange = {
                                registerViewmodel.registerData =
                                    registerViewmodel.registerData.copy(confirmPassword = it)
                            },
                            label = "Confirm Password",
                            leadingIcon = R.drawable.outline_lock_24,
                            trailingIcon = {
                                IconButton(onClick = {
                                    registerViewmodel.toggleConfirmPasswordVisibility()
                                }) {
                                    Icon(
                                        painter = painterResource(id = registerViewmodel.iconConfirmPassword()),
                                        contentDescription = "Toggle password visibility",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            },
                            visualTransformation = if (registerViewmodel.confirmPasswordVisible) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Action buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                            , verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(

                                modifier = Modifier.size(50.dp)
                                    .clickable {
                                        coroutineScope.launch {
                                            registerViewmodel.goBack(navController)
                                        }
                                    }
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceVariant
                                        , shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center

                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(32.dp)
                                )

                            }

                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        registerViewmodel.register(navController)
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp),
                                shape = RoundedCornerShape(28.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 8.dp
                                )
                            ) {
                                Text(
                                    text = "Create Account",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Login link
                        Row(
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = false, radius = 100.dp)
                                ) {
                                    registerViewmodel.goToLogin(navController)
                                }
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Already have an account? ",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Sign In",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

        }
    }
}

@Composable
private fun EnhancedSelectionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: Int,
    isSelected: Boolean,
    gradientColors: List<Color>,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(dampingRatio = 0.7f),
        label = "card_scale"
    )

    val elevation by animateFloatAsState(
        targetValue = if (isSelected) 12f else 4f,
        animationSpec = tween(300),
        label = "card_elevation"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple()
            ) { onClick() }
            .height(110.dp)
            .background(
                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.9f),
                shape = RoundedCornerShape(20.dp),
            ).border(if (isSelected) {
                BorderStroke(
                    3.dp,
                    Brush.linearGradient(gradientColors)
                )
            } else BorderStroke(1.dp,Color.Gray),
                shape = RoundedCornerShape(20.dp)
            )
        ,

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        brush = if (isSelected) {
                            Brush.linearGradient(gradientColors)
                        } else {
                            Brush.linearGradient(
                                listOf(
                                    Color.Gray.copy(alpha = 0.2f),
                                    Color.Gray.copy(alpha = 0.1f)
                                )
                            )
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = if (isSelected) Color.White else Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = if (isSelected) gradientColors[0] else Color.Black,
                textAlign = TextAlign.Center
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = if (isSelected) gradientColors[0].copy(alpha = 0.8f) else Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun EnhancedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: Int? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = leadingIcon?.let { iconRes ->
            {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    RegisterView()
}