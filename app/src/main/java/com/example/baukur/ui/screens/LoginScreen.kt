package com.example.baukur.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baukur.api.network.RetrofitInstance
import com.example.baukur.ui.theme.BaukurTheme
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody

@Composable
fun LoginScreen(onNavigateToRegister: () -> Unit, onNavigateToHome: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val composableScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "🐷",
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    try {
                        composableScope.launch {
                            val res = RetrofitInstance.api.login(
                                RequestBody.create(MediaType.parse("text/plain"), email),
                                RequestBody.create(MediaType.parse("text/plain"), password))
                            println(res)
                            onNavigateToHome()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
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

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen(onNavigateToRegister = {}, onNavigateToHome = {})
}
