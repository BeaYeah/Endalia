package com.beayeah.endalia.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.beayeah.endalia.R
import com.beayeah.endalia.entities.User
import com.beayeah.endalia.utils.Utils
import com.beayeah.endalia.viewModels.UserViewModel

@Composable
fun LoginScreen(userViewModel: UserViewModel, navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoginScreen by remember { mutableStateOf(true) }
    var message by remember { mutableStateOf("") }
    var showRegisterButton by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val fairBlue = colorResource(id = R.color.fair_blue)
        val endaliaGrey = colorResource(id = R.color.endalia_grey)
        val errorRed = colorResource(id = R.color.error_red)
        val invalidCredentials = stringResource(id = R.string.invalid_credentials)
        val loginSuccessFull = stringResource(id = R.string.login_successfull)
        val passwordsNotMatch = stringResource(id = R.string.passwords_not_match)
        val invalidMailFormat = stringResource(id = R.string.invalid_mail_format)
        val passwordRequirements = stringResource(id = R.string.password_requirements)
        val userRegistered = stringResource(id = R.string.user_registered)

        val image: Painter = painterResource(id = R.drawable.endalia_logo)
        Image(
            painter = image,
            contentDescription = stringResource(id = R.string.logo),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(100.dp)
                .padding(bottom = 20.dp, top = 35.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            val emailIcon: Painter =
                painterResource(id = R.drawable.ic_user)
            Image(
                painter = emailIcon,
                contentDescription = stringResource(id = R.string.ic_mail_str),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(id = R.string.email), color = endaliaGrey) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedSupportingTextColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            val passwordIcon: Painter =
                painterResource(id = R.drawable.ic_password)
            Image(
                painter = passwordIcon,
                contentDescription = stringResource(id = R.string.ic_password_str),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(id = R.string.password), color = endaliaGrey) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedSupportingTextColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            )
        }

        if (!isLoginScreen) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                val passwordIcon: Painter =
                    painterResource(id = R.drawable.ic_password)
                Image(
                    painter = passwordIcon,
                    contentDescription = stringResource(id = R.string.ic_password_str),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = {
                        Text(
                            stringResource(id = R.string.confirm_password),
                            color = endaliaGrey
                        )
                    },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedSupportingTextColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                if (isLoginScreen) {
                    userViewModel.getUserByEmail(email) { user ->
                        message = if (user == null || user.password != password) {
                            invalidCredentials
                        } else {
                            navController.navigate("employeeDirectoryScreen")
                            loginSuccessFull
                        }
                    }
                } else {
                    if (password != confirmPassword) {
                        message = passwordsNotMatch
                    } else if (!Utils.isValidEmail(email)) {
                        message = invalidMailFormat
                    } else if (!Utils.isValidPassword(password)) {
                        message =
                            passwordRequirements
                    } else {
                        userViewModel.insert(User(email = email, password = password))
                        message = userRegistered
                        isLoginScreen = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = endaliaGrey
            ),
            border = BorderStroke(1.dp, endaliaGrey),
            shape = RoundedCornerShape(18)
        ) {
            Text(
                text = if (isLoginScreen) stringResource(id = R.string.login) else stringResource(
                    id = R.string.register
                )
            )
        }

        if (showRegisterButton) {
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isLoginScreen = !isLoginScreen
                    showRegisterButton = false
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = fairBlue),
                shape = RoundedCornerShape(18)
            ) {
                Text(
                    text = if (isLoginScreen) stringResource(id = R.string.register_msg) else stringResource(
                        id = R.string.login_msg
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = message, color = errorRed)

        Spacer(modifier = Modifier.weight(1f))

        val bottomImage: Painter =
            painterResource(id = R.drawable.ic_powered_by)
        Image(
            painter = bottomImage,
            contentDescription = stringResource(id = R.string.powered_by),
            modifier = Modifier
                .height(100.dp)
                .align(Alignment.Start)
        )
    }
}
