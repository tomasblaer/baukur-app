package com.example.baukur.ui.screens

import com.example.baukur.data.UserDBHelper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baukur.api.entities.CreateUserPayload
import com.example.baukur.api.network.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(onNavigateToLogin: () -> Unit, onNavigateToEditProfile: () -> Unit, onNavigateToProfile: () -> Unit) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var newSpendingLimit by remember { mutableStateOf("") }
    val dbHelper = UserDBHelper(context)
    val composableScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val res = RetrofitInstance.api.getUser()
        res.body()?.let {
            email = it.email
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            ProfileInfo(label = "Email", value = email)
            Spacer(modifier = Modifier.height(10.dp))
            ProfileInfo(label="Monthly spending limit", value = dbHelper.getUserSpending(email).toString())
            TextField(
                value = newSpendingLimit,
                onValueChange = { newSpendingLimit = it },
                label = { Text("Update spending limit") }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    composableScope.launch {
                        dbHelper.insertOrUpdateUserData(email, newSpendingLimit.toInt())
                    }
                    onNavigateToProfile()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF4081),
                    contentColor = Color.White
                )
            ) {
                Text("Update Spending Limit")
            }
            Spacer(modifier = Modifier.height(5.dp))
            Button(
                onClick = {
                    composableScope.launch {
                        RetrofitInstance.api.logout()
                    }
                    onNavigateToLogin()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF4081),
                    contentColor = Color.White
                )
            ) {
                Text("Logout")
            }
            Spacer(modifier = Modifier.height(5.dp))
            Button(
                onClick = onNavigateToEditProfile,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF4081),
                    contentColor = Color.White
                )
            ) {
                Text("Edit Profile")
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

@Composable
fun EditProfileScreen(onSaveProfile: () -> Unit, onCancel: () -> Unit) {
    val composableScope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val res = RetrofitInstance.api.getUser()
        res.body()?.let {
            email = it.email
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
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
            Text(text = "Edit Profile", fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Change password") }
            )
            Spacer(modifier = Modifier.height(24.dp))


            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {
                        composableScope.launch {
                            RetrofitInstance.api.updateUser(CreateUserPayload(email, newPassword))
                            RetrofitInstance.api.logout()
                        }
                        onSaveProfile()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF4081),
                        contentColor = Color.White
                    )
                ) {
                    Text("Save")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.White
                    )
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileScreen(
        onNavigateToLogin = {},
        onNavigateToEditProfile = {},
        onNavigateToProfile = {}
    )
}

@Preview(showBackground = true)
@Composable
fun EditProfilePreview() {
    EditProfileScreen(
        onSaveProfile = {},
        onCancel = {}
    )
}



