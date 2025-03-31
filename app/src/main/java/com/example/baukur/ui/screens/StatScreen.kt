package com.example.baukur.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.example.baukur.ui.common.PieChartComposable

@Composable
fun StatScreen() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Text("Stat Screen")
    }
    PieChartComposable()
}
