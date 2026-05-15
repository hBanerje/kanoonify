package com.multiplatform.kanoonify.presentation.screens.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoryScreen() {
    val categories = listOf(
        "Traffic Rules",
        "Police Rights",
        "Women Safety",
        "Public Safety"
    )

    LazyColumn {
        items(categories) { category ->
            Text(
                text = category,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
        }
    }
}