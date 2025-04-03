package com.example.baukur.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baukur.api.entities.Category
import com.example.baukur.api.entities.CreateExpensePayload
import com.example.baukur.api.entities.EditExpensePayload
import com.example.baukur.api.entities.Expense
import com.example.baukur.api.network.RetrofitInstance
import com.example.baukur.ui.screens.expenses.EditExpenseDialog
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun HomeScreen(snackbarHostState: SnackbarHostState) {
    var categories by remember { mutableStateOf(emptyList<Category>()) }
    var composableScope = rememberCoroutineScope()
    var showEditDialog by remember { mutableStateOf(false) }
    var expenseToEdit by remember { mutableStateOf<Expense?>(null) }

    LaunchedEffect(Unit) {
        try {
            val res = RetrofitInstance.api.getCategories()
            res.body()?.let {
                categories = it
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val allExpenses = categories.flatMap { category ->
        category.expenses.map { expense ->
            expense to category.name
        }
    }

    val expensesByDate = allExpenses.groupBy { (expense, _) -> expense.date }
        .mapValues { (_, expenses) ->
            expenses.sortedByDescending { (expense, _) -> expense.date }
        }
        .toList()
        .sortedByDescending { (date, _) -> date }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        expensesByDate.forEach { (date, expensesForDate) ->
            item {
                Text(
                    text = "📅 $date",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            itemsIndexed(expensesForDate) { index, pair ->
                val (expense, categoryName) = pair

                SwipeableExpenseItem(
                    expense = expense,
                    categoryName = categoryName,
                    onEdit = {
                        expenseToEdit = expense
                        showEditDialog = true
                    },
                    onDelete = {
                        composableScope.launch {
                            RetrofitInstance.api.deleteExpense(expense.id)
                            snackbarHostState.showSnackbar(
                                message = "Deleted expense: ${expense.name}",
                                duration = SnackbarDuration.Short
                            )
                            try {
                                val res = RetrofitInstance.api.getCategories()
                                res.body()?.let {
                                    categories = it
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                )
            }

            item {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }

    if (showEditDialog && expenseToEdit != null) {
        EditExpenseDialog(
            expense = expenseToEdit!!,
            onDismiss = { showEditDialog = false },
            onSave = { updatedExpense ->
                composableScope.launch {
                    RetrofitInstance.api.editExpense(
                        EditExpensePayload(
                            updatedExpense.id,
                            updatedExpense.name,
                            updatedExpense.amount,
                            updatedExpense.comment,
                            updatedExpense.date,
                            updatedExpense.categoryId
                        )
                    )
                    snackbarHostState.showSnackbar(
                        message = "Edited expense: ${updatedExpense.name}",
                        duration = SnackbarDuration.Short
                    )
                    showEditDialog = false
                    try {
                        val res = RetrofitInstance.api.getCategories()
                        res.body()?.let {
                            categories = it
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        )
    }
}

// Function to make expenses into items and then moveable

@Composable
fun SwipeableExpenseItem(
    expense: Expense,
    categoryName: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    val swipeThreshold = 100f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount
                    if (offsetX > swipeThreshold) {
                        onEdit()
                        offsetX = 0f
                    } else if (offsetX < -swipeThreshold) {
                        onDelete()
                        offsetX = 0f
                    }
                }
            }
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .background(Color(0xFFF5F5F5))
            .padding(12.dp)
    ) {
        Column {
            Text(text = "📝 ${expense.name}", fontWeight = FontWeight.Bold)
            Text(text = "💸 Amount: ${expense.amount}")
            Text(text = "🏷️ Category: $categoryName")
            Text(text = "📅 Date: ${expense.date}")
        }
    }
}






