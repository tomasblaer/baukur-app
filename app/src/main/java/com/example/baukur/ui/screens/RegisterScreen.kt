package com.example.baukur.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.baukur.ui.theme.BaukurTheme

@Composable
fun RegisterScreen() {
    BaukurTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                TextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Username") },
                )
            }
            // TODO: Bara mocked stuff hér, breyta þessu i eitthvað svipað og register routeið á vefsíðunni
        }
    }
}
