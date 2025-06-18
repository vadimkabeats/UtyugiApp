package com.example.utyugiapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun MainScreen(
    irons: List<IronModel>,
    onSortPriceAsc: () -> Unit,
    onSortPriceDesc: () -> Unit,
    onSortModelAsc: () -> Unit,
    onSortModelDesc: () -> Unit,
    onFilterClick: (Double, Double) -> Unit,
    onResetFilterClick: () -> Unit,
    onAddClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onItemClick: (IronModel) -> Unit
) {
    var searchQuery      by remember { mutableStateOf("") }
    var showSortDialog   by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var minStr           by remember { mutableStateOf("") }
    var maxStr           by remember { mutableStateOf("") }
    var sortChoice       by remember { mutableStateOf(0) }

    val filtered = irons.filter {
        it.model.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.iron_image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
        Spacer(Modifier.height(8.dp))

        // Поиск
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Поиск") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        Column(Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { showSortDialog = true })   { Text("Сорт") }
                Button(onClick = { showFilterDialog = true }) { Text("Фильтр") }
            }
            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onAddClick)     { Text("Добавить") }
                Button(onClick = onRefreshClick) { Text("Обновить") }
            }
        }
        Spacer(Modifier.height(16.dp))

        LazyColumn(Modifier.fillMaxSize()) {
            items(filtered) { iron ->
                Text(
                    text = "${iron.model} — ${iron.price}₽",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(iron) }
                        .padding(vertical = 8.dp)
                )
                Divider()
            }
        }
    }

    if (showSortDialog) {
        val options = listOf(
            "Цена: по возрастанию",
            "Цена: по убыванию",
            "Модель: A→Z",
            "Модель: Z→A"
        )
        AlertDialog(
            onDismissRequest = { showSortDialog = false },
            title   = { Text("Сортировка") },
            text    = {
                Column {
                    options.forEachIndexed { index, label ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { sortChoice = index }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = (sortChoice == index),
                                onClick = { sortChoice = index }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    when (sortChoice) {
                        0 -> onSortPriceAsc()
                        1 -> onSortPriceDesc()
                        2 -> onSortModelAsc()
                        3 -> onSortModelDesc()
                    }
                    showSortDialog = false
                }) {
                    Text("Применить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSortDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title   = { Text("Фильтрация по цене") },
            text    = {
                Column {
                    OutlinedTextField(
                        value = minStr,
                        onValueChange = { minStr = it },
                        label = { Text("Мин. цена") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = maxStr,
                        onValueChange = { maxStr = it },
                        label = { Text("Макс. цена") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val min = minStr.toDoubleOrNull() ?: 0.0
                    val max = maxStr.toDoubleOrNull() ?: Double.MAX_VALUE
                    onFilterClick(min, max)
                    showFilterDialog = false
                }) {
                    Text("Применить")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onResetFilterClick()
                    showFilterDialog = false
                }) {
                    Text("Сбросить")
                }
            }
        )
    }
}
