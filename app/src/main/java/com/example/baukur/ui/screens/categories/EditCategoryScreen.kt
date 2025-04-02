package com.example.baukur.ui.screens.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.baukur.api.entities.CreateCategoryPayload
import com.example.baukur.api.network.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun EditCategoryScreen(snackbarHostState: SnackbarHostState, onNavigateToCreateExpense: () -> Unit) {
    var categoryName by remember { mutableStateOf("") }
    var categoryDescription by remember { mutableStateOf("") }
    val composableScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TextField(
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text("Category name") }
            )
            TextField(
                value = categoryDescription,
                onValueChange = { categoryDescription = it },
                label = { Text("Description") }
            )
            Button(
                enabled = categoryName.isNotBlank(),
                onClick = {
                    composableScope.launch {
                        try {
                            val res = RetrofitInstance.api.createCategory(
                                CreateCategoryPayload(
                                    categoryName,
                                    categoryDescription
                                )
                            )
                            if (res.isSuccessful) {
                                categoryName = ""
                                categoryDescription = ""
                                val result = snackbarHostState.showSnackbar(
                                    message = "Category created",
                                    actionLabel = "Back to create expense",
                                    duration = SnackbarDuration.Indefinite,
                                )
                                when (result) {
                                    SnackbarResult.ActionPerformed -> {
                                        onNavigateToCreateExpense()
                                    }
                                    SnackbarResult.Dismissed -> {}
                                }

                            }
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Failed to add expense")
                        }
                    }
                }
            ) {
                Text("Add category")
            }
        }
    }
}