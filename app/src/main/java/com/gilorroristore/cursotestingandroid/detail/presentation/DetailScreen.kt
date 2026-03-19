package com.gilorroristore.cursotestingandroid.detail.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gilorroristore.cursotestingandroid.core.presentation.components.MarketTopAppBar

@Composable
fun DetailScreen(modifier: Modifier = Modifier, id: String, navBack: () -> Unit) {
    Scaffold(
        topBar = {
            MarketTopAppBar("Detalle") {
                navBack()
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Detail screen with $id")
        }
    }
}