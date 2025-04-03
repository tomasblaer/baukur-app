package com.example.baukur.ui.common
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import android.graphics.Color
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.example.baukur.api.entities.Category
import com.example.baukur.api.network.RetrofitInstance
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


@Composable
fun PieChartComposable() {
    val categories = remember { mutableStateListOf<Category>() }
    val pieEntries = remember { mutableStateListOf<PieEntry>() }

    LaunchedEffect(Unit) {
        fetchCategories(categories)
        categories.forEach { category ->
            val amount = category.expenses.sumOf { it.amount }
            pieEntries.add(PieEntry(amount.toFloat(), category.name))
        }
    }

    if (pieEntries.isNotEmpty()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                PieChart(context).apply {
                    val dataSet = PieDataSet(pieEntries, "Categories").apply {
                        colors = listOf(Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW)
                        valueTextSize = 14f
                        valueTextColor = Color.WHITE
                    }

                    val pieData = PieData(dataSet)

                    data = pieData
                    description.isEnabled = false
                    centerText = "Categories"
                    setEntryLabelColor(Color.BLACK)
                    animateY(1000)

                    legend.apply {
                        orientation = Legend.LegendOrientation.VERTICAL
                        isWordWrapEnabled = true
                        textColor = Color.BLACK
                    }
                }
            }
        )
    }
}

suspend fun fetchCategories(categories: MutableList<Category>) {
    try {
        val res = RetrofitInstance.api.getCategories()
        res.body()?.let {
            categories.clear()
            categories.addAll(it)  // Update the state list to trigger recomposition
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}