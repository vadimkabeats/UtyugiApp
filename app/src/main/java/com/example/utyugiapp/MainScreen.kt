package com.example.utyugiapp

import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current

    // 1) инициализируем SoundPool для click.mp3
    val soundPool = remember {
        SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .build()
    }
    // 2) загружаем click.mp3 из res/raw
    val soundId = remember { soundPool.load(context, R.raw.click, 1) }
    // 3) освобождаем при уничтожении
    DisposableEffect(Unit) {
        onDispose { soundPool.release() }
    }

    var searchQuery      by remember { mutableStateOf("") }
    var showSortDialog   by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var minStr           by remember { mutableStateOf("") }
    var maxStr           by remember { mutableStateOf("") }
    var sortChoice       by remember { mutableStateOf(0) }

    val filtered = irons.filter { it.model.contains(searchQuery, ignoreCase = true) }

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

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Поиск") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // --- Два ряда кнопок ---
        Column(Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = {
                    // звук + открыть диалог сортировки
                    soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
                    showSortDialog = true
                }) {
                    Text("Сортировка")
                }
                Button(onClick = {
                    soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
                    showFilterDialog = true
                }) {
                    Text("Фильтр")
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = {
                    soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
                    onAddClick()
                }) {
                    Text("Добавить")
                }
                Button(onClick = {
                    soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
                    onRefreshClick()
                }) {
                    Text("Обновить")
                }
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
                        .clickable {
                            soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
                            onItemClick(iron)
                        }
                        .padding(vertical = 8.dp)
                )
                Divider()
            }
        }
    }

    // --- Диалог сортировки ---
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
                    options.forEachIndexed { idx, lbl ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { sortChoice = idx }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = sortChoice == idx,
                                onClick = { sortChoice = idx }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(lbl)
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

    // --- Диалог фильтрации ---
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
