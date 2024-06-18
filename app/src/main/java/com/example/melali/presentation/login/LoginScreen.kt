package com.example.melali.presentation.login

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.melali.R
import com.example.melali.model.request.LoginRequest
import com.example.melali.model.response.UserResponse
import com.example.melali.util.SnackbarHandler

@Composable
fun LoginScreen(navController: NavController) {
    val viewModel = hiltViewModel<LoginViewModel>()

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }



    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.text_logo),
            contentDescription = "",
            alignment = Alignment.TopStart,
            modifier = Modifier.fillMaxSize()
        )
        Image(
            painter = painterResource(id = R.drawable.bg_login),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Login",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )

            Text(text = "Welcome Back!", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(value = email, onValueChange = {
                email = it
            }, label = {
                Text(text = "Email address")
            })

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = password, onValueChange = {
                password = it
            }, label = {
                Text(text = "Password")
            }, visualTransformation = PasswordVisualTransformation())

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp), contentAlignment = Alignment.TopStart
            ) {
                Text(text = "Forgot Password", color = Color.Blue, modifier = Modifier.clickable {
                    //////////// DO SOMETHING
                })
            }




            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val body = LoginRequest(email, password)
                    Log.d("Check", email)
                    Log.d("Check", password)
                    viewModel.login(body,
                        onSuccess = {
                            SnackbarHandler.showSnackbar("Berhasil Login")
                            it.data?.let {res ->
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
                            navController.navigate("home"){
                                popUpTo(navController.graph.id){
                                    inclusive = true
                                }
                            }
                        },
                        onFailed = {
                            SnackbarHandler.showSnackbar(it.message.toString())
                        })
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Text(text = "Login")
            }


            Spacer(modifier = Modifier.height(48.dp))
            Text(text = "Or continue with")
            Spacer(modifier = Modifier.height(12.dp))

            Row {
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(Int.MAX_VALUE.dp),
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
                    onClick = {},
                    shape = RoundedCornerShape(Int.MAX_VALUE.dp),
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

            Spacer(modifier = Modifier.height(48.dp))

            Row {
                Text(text = "Don't have account?")
                Text(text = "Create Now", color = Color.Blue, modifier = Modifier.clickable {
                    navController.navigate("register")
                    navController.popBackStack()
                })
            }
        }
    }

}