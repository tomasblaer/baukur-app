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
import androidx.compose.runtime.remember
import com.example.baukur.api.entities.Category
import com.example.baukur.api.network.RetrofitInstance
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


@Composable
fun PieChartComposable() {
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
    val pieEntries = categories.map {
        val amount = it.expenses.sumOf { expense -> expense.amount }
        PieEntry(amount.toFloat(), it.name)
    }


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
