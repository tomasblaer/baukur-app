package com.example.baukur.ui.screens.expenses

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import com.example.baukur.api.entities.Expense

@Composable
fun EditExpenseDialog(
    expense: Expense,
    onDismiss: () -> Unit,
    onSave: (Expense) -> Unit
) {
    var name by remember { mutableStateOf(expense.name) }
    var amount by remember { mutableStateOf(expense.amount) }
    var comment by remember { mutableStateOf(expense.comment) }
    var date by remember { mutableStateOf(expense.date) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Expense") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                TextField(
                    value = amount.toString(),
                    onValueChange = { amount = it.toDoubleOrNull() ?: amount },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                TextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Comment") }
                )
                TextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(expense.copy(name = name, amount = amount, comment = comment, date = date))
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}