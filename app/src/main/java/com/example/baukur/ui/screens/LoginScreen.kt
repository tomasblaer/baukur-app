package com.example.baukur.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.baukur.ui.theme.BaukurTheme

@Composable
fun LoginScreen(onNavigateToRegister: () -> Unit) {
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
    BaukurTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    UsernameField()
                    Spacer(modifier = Modifier.height(8.dp))
                    PasswordField()
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {

                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF4081),
                            contentColor = Color.White)
                    ) {
                        Text("Login")
                    }
                    TextButton(onClick = onNavigateToRegister) {
                        Text("Don't have an account? ", color = Color.Gray)
                        Text("Register")
                    }

                }
            }
        }
    }
}



@Composable
fun UsernameField(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    TextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email") },
        modifier = modifier
    )
}

@Composable
fun PasswordField(modifier: Modifier = Modifier) {
    var password by remember { mutableStateOf("") }
    TextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen(onNavigateToRegister = {})
}
