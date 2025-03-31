package com.example.baukur.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baukur.api.entities.Category
import com.example.baukur.api.entities.Expense
import com.example.baukur.api.network.RetrofitInstance


@Composable
fun HomeScreen() {
    var categories by remember { mutableStateOf(emptyList<Category>()) }

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
                        println("Edit: ${expense.name}")
                        // TODO: Open edit dialog or navigate
                    },
                    onDelete = {
                        println("Delete: ${expense.name}")
                        // TODO: Handle deletion logic here
                    }
                )
            }

            item {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
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







