package com.example.baukur.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.baukur.ui.theme.BaukurTheme

@Composable
fun LoginScreen(onNavigateToRegister: () -> Unit) {
    BaukurTheme {
        Scaffold (
            modifier = Modifier.fillMaxSize()

        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                UsernameField()
                PasswordField()
                Button(onClick = onNavigateToRegister) {
                    Text("Register")
                }
            }
        }
    }
}

@Composable
fun UsernameField(modifier: Modifier = Modifier) {
    Column {
        var username by remember { mutableStateOf("") }
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = modifier
        )
    }
}

@Composable
fun PasswordField(modifier: Modifier = Modifier) {
    Column {
        var password by remember { mutableStateOf("") }
//        var passwordVisibility: Boolean by remember { mutableStateOf(false) }
        TextField(
//            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen(onNavigateToRegister = {})
}