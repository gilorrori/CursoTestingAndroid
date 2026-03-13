package com.gilorroristore.cursotestingandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gilorroristore.cursotestingandroid.core.presentation.navigation.NavGraph
import com.gilorroristore.cursotestingandroid.productlist.presentation.ProductListScreen
import com.gilorroristore.cursotestingandroid.ui.theme.CursoTestingAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CursoTestingAndroidTheme {

                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    NavGraph()
                    //ProductListScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}