package com.example.utyugiapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddIronScreen(
    onSave: (IronModel) -> Unit,
    onCancel: () -> Unit
) {
    var model          by remember { mutableStateOf("") }
    var color          by remember { mutableStateOf("") }
    var power          by remember { mutableStateOf("") }
    var soleType       by remember { mutableStateOf("") }
    var steamRate      by remember { mutableStateOf("") }
    var steamBoost     by remember { mutableStateOf("") }
    var steamControl   by remember { mutableStateOf("") }
    var tankVolume     by remember { mutableStateOf("") }
    var leakProtect    by remember { mutableStateOf("") }
    var waterLevel     by remember { mutableStateOf("") }
    var vertical       by remember { mutableStateOf("") }
    var maxPressure    by remember { mutableStateOf("") }
    var antiScale      by remember { mutableStateOf("") }
    var safety         by remember { mutableStateOf("") }
    var weight         by remember { mutableStateOf("") }
    var quantity       by remember { mutableStateOf("") }
    var price          by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Добавить новый утюг", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = model,
            onValueChange = { model = it },
            label = { Text("Модель") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = color,
            onValueChange = { color = it },
            label = { Text("Цвет") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = power,
            onValueChange = { power = it },
            label = { Text("Мощность (Вт)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = soleType,
            onValueChange = { soleType = it },
            label = { Text("Тип подошвы") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = steamRate,
            onValueChange = { steamRate = it },
            label = { Text("Интенсивность пара (г/мин)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = steamBoost,
            onValueChange = { steamBoost = it },
            label = { Text("Паровой удар (Есть/Нет)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = steamControl,
            onValueChange = { steamControl = it },
            label = { Text("Регулировка пара (Есть/Нет)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = tankVolume,
            onValueChange = { tankVolume = it },
            label = { Text("Объем резервуара (мл)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = leakProtect,
            onValueChange = { leakProtect = it },
            label = { Text("Защита от протечек (Есть/Нет)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = waterLevel,
            onValueChange = { waterLevel = it },
            label = { Text("Индикатор уровня воды (Есть/Нет)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = vertical,
            onValueChange = { vertical = it },
            label = { Text("Вертикальное отпаривание (Есть/Нет)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = maxPressure,
            onValueChange = { maxPressure = it },
            label = { Text("Макс. давление пара (бар)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = antiScale,
            onValueChange = { antiScale = it },
            label = { Text("Защита от накипи (Есть/Нет)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = safety,
            onValueChange = { safety = it },
            label = { Text("Система безопасности") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Вес (кг)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Количество") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Цена (руб)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onCancel) {
                Text("Отмена")
            }
            Button(onClick = {
                val newIron = IronModel(
                    number       = 0,
                    model        = model,
                    color        = color,
                    power        = power.toIntOrNull() ?: 0,
                    soleType     = soleType,
                    steamRate    = steamRate.toIntOrNull() ?: 0,
                    steamBoost   = steamBoost.equals("Есть", true),
                    steamControl = steamControl.equals("Есть", true),
                    tankVolume   = tankVolume.toIntOrNull() ?: 0,
                    leakProtect  = leakProtect.equals("Есть", true),
                    waterLevel   = waterLevel.equals("Есть", true),
                    vertical     = vertical.equals("Есть", true),
                    maxPressure  = maxPressure.replace(',', '.').toDoubleOrNull() ?: 0.0,
                    antiScale    = antiScale.equals("Есть", true),
                    safety       = safety,
                    weight       = weight.replace(',', '.').toDoubleOrNull() ?: 0.0,
                    quantity     = quantity.toIntOrNull() ?: 0,
                    price        = price.replace(',', '.').toDoubleOrNull() ?: 0.0
                )
                onSave(newIron)
            }) {
                Text("Сохранить")
            }
        }
    }
}
