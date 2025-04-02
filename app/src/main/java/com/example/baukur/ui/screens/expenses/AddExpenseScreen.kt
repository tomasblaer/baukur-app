package com.example.baukur.ui.screens.expenses

import android.R
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baukur.api.entities.Category
import com.example.baukur.api.entities.CreateExpensePayload
import com.example.baukur.api.network.RetrofitInstance
import com.example.baukur.data.UserDBHelper
import com.example.baukur.ui.common.DatePickerFieldToModal
import com.example.baukur.ui.common.convertMillisToDate
import kotlinx.coroutines.launch

@Composable
fun AddExpenseScreen(snackbarHostState: SnackbarHostState, navigateToNewCategory: () -> Unit, navigateToEditCategory: () -> Unit) {
    var userCategories by remember { mutableStateOf(emptyList<Category>()) }
    var expenseName by remember { mutableStateOf("") }
    var expenseComment by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableLongStateOf(0) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    val context = LocalContext.current
    val dbHelper = UserDBHelper(context)
    var spendingLimit by remember { mutableIntStateOf(0) }
    val composableScope = rememberCoroutineScope()

    var selectedDate by remember { mutableStateOf<Long?>(null) }
    LaunchedEffect(Unit) {
        val res = RetrofitInstance.api.getCategories()
        res.body()?.let {
            userCategories = it
        }
        val user = RetrofitInstance.api.getUser().body()
        if (user != null) {
            dbHelper.getUserSpending(user.email)?.let {
                spendingLimit = it
            }
        }
    }
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
                value = expenseName,
                onValueChange = { expenseName = it },
                label = { Text("Expense name") }
            )
            TextField(
                value = expenseComment,
                onValueChange = { expenseComment = it },
                label = { Text("Comment") }
            )
            TextField(
                value = expenseAmount.toString(),
                onValueChange = { expenseAmount = it.toLongOrNull() ?: expenseAmount
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                label = { Text("Amount (isk)") }
            )

            DatePickerFieldToModal(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it }
            )

            CategoryPicker(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it },
                categoryOptions = userCategories,
                onNewCategory = { navigateToNewCategory() },
                onEditCategory = { navigateToEditCategory() }
            )

            Button(
                enabled = selectedDate != null && selectedCategory != null && expenseName != "",
                onClick = {
                    composableScope.launch {
                        try {
                            println(convertMillisToDate(selectedDate!!))
                            val res = RetrofitInstance.api.createExpense(
                                CreateExpensePayload(
                                    expenseName,
                                    expenseAmount.toDouble(),
                                    expenseComment,
                                    convertMillisToDate(selectedDate!!),
                                    selectedCategory!!.id
                                )
                            )
                            if (res.isSuccessful) {
                                expenseName = ""
                                expenseAmount = 0
                                expenseComment = ""
                                selectedDate = null
                                selectedCategory = null
                                snackbarHostState.showSnackbar("Expense added")

                            }
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Failed to add expense")
                        }
                    }
                }
            ) {
                Text("Add expense")
            }
            if (expenseAmount > spendingLimit) {
                Text(
                    "This expense exceeds your spending limit!",
                    color = colorResource(id = R.color.darker_gray),
                    fontSize = 12.sp
                )
            }
        }

    }
}

@Composable
fun CategoryPicker(
        selectedCategory: Category?,
        onCategorySelected: (Category) -> Unit,
        categoryOptions: List<Category>,
        onEditCategory: () -> Unit,
        onNewCategory: () -> Unit
    ) {
    var showCategoryMenu by remember { mutableStateOf(false) }
    Box {
        OutlinedTextField(
            value = selectedCategory?.name ?: "",
            onValueChange = { },
            label = { Text("Category") },
            readOnly = true,
            supportingText = {
                Text(
                    text = selectedCategory?.description ?: "Press + to add new category",
                    color = colorResource(id = R.color.darker_gray)
                )
            },
            trailingIcon = {
                Row {
                    if (selectedCategory != null) {
                        IconButton(
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit category"
                                )
                            },
                            onClick = {
                                onEditCategory()
                            }
                        )
                    }
                    IconButton (
                        content = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add category"
                            )
                        },
                        onClick = {
                            showCategoryMenu = false
                            onNewCategory()
                        }
                    )
                }
            },
            modifier = Modifier
                .pointerInput(Unit) {
                    awaitEachGesture {
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) {
                            showCategoryMenu = true
                        }
                    }
                }
        )
        DropdownMenu(
            expanded = showCategoryMenu,
            onDismissRequest = { showCategoryMenu = false }
        ) {
            categoryOptions.forEach { category ->
                DropdownMenuItem(
                    onClick = {
                        onCategorySelected(category)
                        showCategoryMenu = false
                    },
                    text = { Text(category.name) }
                )
            }
        }
    }
}