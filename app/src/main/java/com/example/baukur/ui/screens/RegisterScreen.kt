package com.example.baukur.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.baukur.api.entities.CreateUserPayload
import com.example.baukur.api.network.RetrofitInstance
import com.example.baukur.ui.theme.BaukurTheme
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(onNavigateToLogin: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val composableScope = rememberCoroutineScope();
    val snackbarHostState = remember { SnackbarHostState() }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // why does this not show when below ;(
            RegisterForm(
                snackbarHostState = snackbarHostState,
                onNavigateToLogin = onNavigateToLogin
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onNavigateToLogin) {
                Text("Already have an account? ", color = Color.Gray)
                Text("Log in")
            }
        }
    }
}

@Composable
fun RegisterForm(snackbarHostState: SnackbarHostState, onNavigateToLogin: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val composableScope = rememberCoroutineScope();
    TextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email") },
    )
    TextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
    )
    Spacer(modifier = Modifier.height(8.dp))
    Button(
        onClick = {
            try {
                composableScope.launch {
                    val res = RetrofitInstance.api.createUser(CreateUserPayload(email, password))
                    print(res)
                    val result = snackbarHostState.showSnackbar(
                        message = "User created",
                        actionLabel = "Login",
                        duration = SnackbarDuration.Indefinite,
                    )
                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            onNavigateToLogin()
                        }
                        SnackbarResult.Dismissed -> {}
                    }
                }
            } catch (e: Exception) {
                composableScope.launch {
                    snackbarHostState.showSnackbar("Failed to create user")
                }
                e.printStackTrace()
            }
        },
        enabled = email.isNotEmpty() && password.isNotEmpty(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFF4081),
            contentColor = Color.White)
    ) {
        Text("Register")
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    RegisterScreen(onNavigateToLogin = {})
}
