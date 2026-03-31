@file:OptIn(ExperimentalMaterial3Api::class)

package com.gilorroristore.cursotestingandroid.productlist.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HomeTopAppBar(
    filtersVisible: Boolean = false,
    onFiltersSelect: (Boolean) -> Unit = {},
    navigateToSettings: () -> Unit,
    navigateToCart: () -> Unit,
    cartItemCount: Int
) {
    TopAppBar(
        title = {
            Text(
                text = "MiniMarket",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),

        actions = {
            BadgedBox(
                modifier = Modifier.padding(end = 4.dp),
                badge = {
                    if (cartItemCount > 0) {
                        Badge {
                            Text(
                                text = if (cartItemCount > 99) {
                                    "+99"
                                } else {
                                    cartItemCount.toString()
                                },
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            ) {
                IconButton(onClick = { navigateToCart() }) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Cart",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            IconButton(onClick = { onFiltersSelect(!filtersVisible) }) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = if (filtersVisible) "Ocultar filtos" else "Mostrar filtros",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            IconButton(onClick = { navigateToSettings() }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    )
}