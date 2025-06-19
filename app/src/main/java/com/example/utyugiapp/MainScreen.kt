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

    // 1) Создаем SoundPool для click.mp3
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
    // 2) Загружаем звук из raw/click.mp3
    val soundId = remember { soundPool.load(context, R.raw.click, 1) }
    // 3) Освобождаем ресурсы при уничтожении
    DisposableEffect(Unit) {
        onDispose { soundPool.release() }
    }

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
                Button(onClick = {
                    soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
                    showSortDialog = true
                }) { Text("Сортировка") }

                Button(onClick = {
                    soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
                    showFilterDialog = true
                }) { Text("Фильтр") }
            }
            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = {
                    soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
                    onAddClick()
                }) { Text("Добавить") }

                Button(onClick = {
                    soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
                    onRefreshClick()
                }) { Text("Обновить") }
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

    // ... остальной код диалогов сортировки и фильтрации без изменений ...
}
