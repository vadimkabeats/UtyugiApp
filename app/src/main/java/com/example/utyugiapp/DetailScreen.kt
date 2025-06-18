package com.example.utyugiapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DetailScreen(iron: IronModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("№: ${iron.number}", style = MaterialTheme.typography.titleMedium)
        Text("Модель: ${iron.model}", style = MaterialTheme.typography.titleMedium)
        Text("Цвет: ${iron.color}", style = MaterialTheme.typography.bodyLarge)
        Text("Мощность: ${iron.power} Вт", style = MaterialTheme.typography.bodyLarge)
        Text("Тип подошвы: ${iron.soleType}", style = MaterialTheme.typography.bodyLarge)
        Text("Интенсивность пара: ${iron.steamRate} г/мин", style = MaterialTheme.typography.bodyLarge)
        Text("Паровой удар: ${if (iron.steamBoost) "Есть" else "Нет"}", style = MaterialTheme.typography.bodyLarge)
        Text("Регулировка пара: ${if (iron.steamControl) "Есть" else "Нет"}", style = MaterialTheme.typography.bodyLarge)
        Text("Объем резервуара: ${iron.tankVolume} мл", style = MaterialTheme.typography.bodyLarge)
        Text("Защита от протечек: ${if (iron.leakProtect) "Есть" else "Нет"}", style = MaterialTheme.typography.bodyLarge)
        Text("Индикатор уровня воды: ${if (iron.waterLevel) "Есть" else "Нет"}", style = MaterialTheme.typography.bodyLarge)
        Text("Вертикальное отпаривание: ${if (iron.vertical) "Есть" else "Нет"}", style = MaterialTheme.typography.bodyLarge)
        Text("Макс. давление пара: ${iron.maxPressure} бар", style = MaterialTheme.typography.bodyLarge)
        Text("Защита от накипи: ${if (iron.antiScale) "Есть" else "Нет"}", style = MaterialTheme.typography.bodyLarge)
        Text("Система безопасности: ${iron.safety}", style = MaterialTheme.typography.bodyLarge)
        Text("Вес: ${iron.weight} кг", style = MaterialTheme.typography.bodyLarge)
        Text("Количество: ${iron.quantity}", style = MaterialTheme.typography.bodyLarge)
        Text("Цена: ${iron.price} ₽", style = MaterialTheme.typography.bodyLarge)
    }
}
