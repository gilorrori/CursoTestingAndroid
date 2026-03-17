package com.gilorroristore.cursotestingandroid.productlist.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gilorroristore.cursotestingandroid.productlist.domain.models.SortOption
import com.gilorroristore.cursotestingandroid.productlist.presentation.ProductListUiState


@Composable
fun FiltersMenu(
    modifier: Modifier = Modifier,
    state: ProductListUiState.Success,
    onCategorySelected: (String?) -> Unit,
    onSortSelected: (SortOption) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Categorias"
            )

            Row(
                modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = state.selectedCategory == null,
                    onClick = {
                        onCategorySelected(null)
                    },
                    label = { Text(text = "Todas", style = MaterialTheme.typography.labelSmall) })
                state.categories?.forEach { category ->
                    FilterChip(
                        selected = category.equals(state.selectedCategory, ignoreCase = true),
                        onClick = {
                            onCategorySelected(category)
                        },
                        label = {
                            Text(text = category, style = MaterialTheme.typography.labelSmall)
                        }
                    )
                }
            }

            HorizontalDivider()

            Text(text = "Ordenar por")
            Row(
                modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    modifier = Modifier.weight(1f),
                    selected = state.sortOption == SortOption.PRICE_ASC,
                    onClick = { onSortSelected(SortOption.PRICE_ASC) },
                    label = { Text(text = "Precio ⬆", style = MaterialTheme.typography.labelSmall) }
                )

                FilterChip(
                    modifier = Modifier.weight(1f),
                    selected = state.sortOption == SortOption.PRICE_DESC,
                    onClick = { onSortSelected(SortOption.PRICE_DESC)},
                    label = { Text(text = "Precio ⬇", style = MaterialTheme.typography.labelSmall) }
                )

                FilterChip(
                    modifier = Modifier.weight(1f),
                    selected = state.sortOption == SortOption.DISCOUNT,
                    onClick = { onSortSelected(SortOption.DISCOUNT)},
                    label = {
                        Text(
                            text = "Descuento",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )
            }
        }
    }
}