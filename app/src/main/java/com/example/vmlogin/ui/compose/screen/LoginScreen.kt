package com.example.vmlogin.ui.compose.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.vmlogin.R
import com.example.vmlogin.ui.compose.screen.viewModel.LoginViewModel
import com.example.vmlogin.ui.theme.VMLoginTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val loginState by viewModel.loginState.collectAsState()

    if (loginState is LoginViewModel.LoginState.Success) {
        navController.navigate("main")
    }

    VMLoginTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            content = {
                Column {
                    ProgressIndicator(loginState)

                    LoginView(viewModel)
                }
            }
        )

        when (loginState) {
            is LoginViewModel.LoginState.Error -> {
                LaunchedEffect(loginState) {
                    scope.launch {
                        ShowSnackbar(
                            snackbarHostState,
                            viewModel,
                            (loginState as LoginViewModel.LoginState.Error).errorMessage
                        )
                    }
                }
            }

            is LoginViewModel.LoginState.UnexpectedError -> {
                val error = stringResource(id = R.string.error_unexpected)
                LaunchedEffect(loginState) {
                    scope.launch {
                        ShowSnackbar(snackbarHostState, viewModel, error)
                    }
                }
            }

            else -> {}
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(
    viewModel: LoginViewModel
) {
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val loginEnabled by remember { derivedStateOf { userName.isNotBlank() && password.isNotBlank() } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = userName,
            onValueChange = {
                userName = it
                viewModel.setUserName(it)
            },
            label = { Text(text = stringResource(id = R.string.username)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = {
                password = it
                viewModel.setPassword(it)
            },
            label = { Text(text = stringResource(id = R.string.password)) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val (icon, iconColor) = if (passwordVisible) {
                    Pair(
                        painterResource(id = R.drawable.ic_visibility),
                        colorResource(id = R.color.purple_200)
                    )
                } else {
                    Pair(
                        painterResource(R.drawable.ic_visibility_off),
                        colorResource(id = R.color.white)
                    )
                }

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = icon, contentDescription = "Visibility", tint = iconColor)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedButton(
            modifier = Modifier
                .align(Alignment.End),
            shape = RoundedCornerShape(5.dp),
            enabled = loginEnabled,
            onClick = {
                viewModel.login()
            }
        ) {
            Text(text = stringResource(id = R.string.login))
        }
    }
}

@Composable
fun ProgressIndicator(loginState: LoginViewModel.LoginState) {
    if (loginState == LoginViewModel.LoginState.Loading) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

suspend fun ShowSnackbar(hostState: SnackbarHostState, viewModel: LoginViewModel, message: String) {
    val snackbarResult = hostState.showSnackbar(
        message = message,
        duration = SnackbarDuration.Indefinite,
        actionLabel = "OK"
    )
    if (snackbarResult == SnackbarResult.ActionPerformed) {
        hostState.currentSnackbarData?.dismiss()
        viewModel.resetState()
    }
}
