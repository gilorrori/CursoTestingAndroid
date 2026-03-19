package com.gilorroristore.cursotestingandroid.detail.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gilorroristore.cursotestingandroid.core.presentation.components.MarketTopAppBar

@Composable
fun DetailScreen(modifier: Modifier = Modifier, id: String, navBack: () -> Unit) {
    Scaffold(
        topBar = { MarketTopAppBar("Detalle")  {
            navBack()
        }}
    ) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {
            Text(text = "Detail screen with $id")
        }

    }
}