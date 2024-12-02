package com.amaurysdm.codequest.ui.login

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.amaurysdm.codequest.R
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun LoginView(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewmodel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
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

                OutlinedTextField(
                    value = loginViewModel.loginData.email,
                    onValueChange = {
                        loginViewModel.loginData = loginViewModel.loginData.copy(email = it)
                    },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = loginViewModel.loginData.password,
                    onValueChange = {
                        loginViewModel.loginData = loginViewModel.loginData.copy(password = it)
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            loginViewModel.togglePasswordVisibility()
                        }) {
                            Icon(
                                painter = painterResource(id = loginViewModel.iconPassword()),
                                contentDescription = "Visibility"
                            )
                        }
                    },
                    visualTransformation = if (loginViewModel.passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                loginViewModel.goBack(navController)
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
                                loginViewModel.login(navController)
                            }

                        }, shape = MaterialTheme.shapes.small, modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Login")
                    }
                }

                Row(
                    modifier = Modifier.clickable {
                        loginViewModel.goToRegister(navController)
                    }

                ) {
                    Text(text = "Already have an account? ")
                    Text(text = "Register", color = MaterialTheme.colorScheme.primary)
                }

            }
        }
    }

}