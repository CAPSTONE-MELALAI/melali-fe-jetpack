package com.example.melali.presentation.register

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
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.core_ui.util.rememberLoadingState
import com.example.melali.R
import com.example.melali.model.request.RegisterRequest
import com.example.melali.model.response.UserResponse
import com.example.melali.util.SnackbarHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val loadingHandler = rememberLoadingState()
    val viewModel = hiltViewModel<RegisterViewModel>()
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val formFilled = remember {
        derivedStateOf {
            username.isNotEmpty()
                    && email.isNotEmpty()
                    && phoneNumber.isNotEmpty()
                    && password.isNotEmpty()
        }
    }
    val categories = listOf(
        "Sejarah",
        "Wisata Alam",
        "Pantai",
        "Hiburan",
        "Taman"
    )
    val categorySelected = remember { mutableStateListOf<Int>() }

    if (viewModel.showSheet.value) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.showSheet.value = false
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Pilih Kategori",
                    style = MaterialTheme.typography.titleMedium
                )

                Column {
                    categories.forEachIndexed { index, s ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Checkbox(
                                checked = categorySelected.contains(index),
                                onCheckedChange = {
                                    if (it) {
                                        categorySelected.add(index)
                                    } else {
                                        categorySelected.remove(index)
                                    }
                                }
                            )

                            Text(text = s)
                        }
                    }
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (categorySelected.size < 3) {
                            SnackbarHandler.showSnackbar("Pilih minimal 3 kategori")
                            return@Button
                        }
                        loadingHandler.show()
                        val body = RegisterRequest(
                            username = username,
                            email = email,
                            password = password,
                            confPassword = password,
                            phoneNumber = phoneNumber,
                            category = categorySelected
                        )
                        viewModel.register(body,
                            onSuccess = {
                                SnackbarHandler.showSnackbar("Berhasil")
                                it.data?.let { res ->
                                    viewModel.saveUserData(
                                        token = res.token,
                                        userData = UserResponse(
                                            uid = res.uid,
                                            phoneNumber = res.phoneNumber,
                                            indexUser = res.indexUser,
                                            category = res.category,
                                            email = res.email,
                                            username = res.username
                                        )
                                    )
                                }
                                loadingHandler.dismissLoading()
                                navController.navigate("home") {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                }
                            },
                            onFailed = {
                                loadingHandler.dismissLoading()
                                viewModel.showSheet.value = false
                                SnackbarHandler.showSnackbar(it.message.toString())
                            }
                        )
                    }
                ) {
                    Text(text = "Lanjutkan")
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_signup),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 40.dp,
                    end = 40.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = R.drawable.text_logo_white,
                contentDescription = "",
                modifier = Modifier.width(160.dp)
            )
            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = "Signup",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )


            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = username, onValueChange = {
                username = it
            }, label = {
                Text(text = "Username")
            })

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = email, onValueChange = {
                email = it
            }, label = {
                Text(text = "Email address")
            })

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = phoneNumber, onValueChange = {
                phoneNumber = it
            }, label = {
                Text(text = "Phone Number")
            })

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(value = password, onValueChange = {
                password = it
            }, label = {
                Text(text = "Password")
            }, visualTransformation = PasswordVisualTransformation())

            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    if (!formFilled.value) {
                        SnackbarHandler.showSnackbar("Pastikan semua data terisi")
                        return@Button
                    }

                    viewModel.showSheet.value = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Sign Up")
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row {
                Text(text = "Already have an account? ")
                Text(text = "Login", color = Color.Blue, modifier = Modifier.clickable {
                    navController.popBackStack()
                    navController.navigate("login")
                })
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .width(140.dp)
                        .height(50.dp)
                ) {
                    AsyncImage(
                        model = R.drawable.ic_google,
                        contentDescription = "",
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp)
                    )
                    Text(text = "Google")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { },
                    modifier = Modifier
                        .width(140.dp)
                        .height(50.dp)
                ) {
                    AsyncImage(
                        model = R.drawable.ic_facebook,
                        contentDescription = "",
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp)
                    )
                    Text(text = "Facebook")
                }
            }
        }
    }
}

@Preview
@Composable
fun miawmiaww() {
    Box(modifier = Modifier.background(Color.White)) {
        RegisterScreen(navController = rememberNavController())
    }
}
