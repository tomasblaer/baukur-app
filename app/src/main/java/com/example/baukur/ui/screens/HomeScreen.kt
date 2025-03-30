package com.example.baukur.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baukur.api.entities.Category
import com.example.baukur.api.entities.DefaultCategory
import com.example.baukur.api.entities.Expense
import com.example.baukur.api.network.RetrofitInstance
import com.google.android.material.progressindicator.CircularProgressIndicator
import org.w3c.dom.Text

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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(text = expense.name)
                    Text(text = "💸 Amount: ${expense.amount}")
                    Text(text = "🏷️ Category: $categoryName")
                }
                // TODO
                // Skoða afh expense dates koma öll undir 29/12/25 þó að maður velur annað
                println("Expense title: ${expense.name}, date: ${expense.date}")
            }

            item {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}




