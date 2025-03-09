package com.example.baukur.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baukur.R
import com.example.baukur.ui.theme.BaukurTheme

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    BaukurTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color(0xFFFAF3F3) // Light pink background
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileInfo(label = "Username", value = "JohnDoe")
                    ProfileInfo(label = "Email", value = "johndoe@example.com")
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onLogout,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF4081), // Pink button
                            contentColor = Color.White
                        )
                    ) {
                        Text("Logout")
                    }
                }
            }
        }
    }
}


@Composable
fun ProfileInfo(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
        Text(text = value, fontSize = 18.sp, color = Color.Black)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileScreen(onLogout = {})
}
