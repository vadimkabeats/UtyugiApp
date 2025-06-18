package com.example.utyugiapp

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _irons = mutableStateListOf<IronModel>()
    val irons: List<IronModel> get() = _irons

    private var originalIrons: List<IronModel> = emptyList()

    fun setIrons(list: List<IronModel>) {
        originalIrons = list.toList()
        _irons.clear()
        _irons.addAll(list)
    }
    fun sortByModelAsc() {
        _irons.sortBy { it.model.lowercase() }
    }

    fun sortByModelDesc() {
        _irons.sortByDescending { it.model.lowercase() }
    }
    fun sortByPriceAsc() {
        _irons.sortBy { it.price }
    }

    fun sortByPriceDesc() {
        _irons.sortByDescending { it.price }
    }

    fun filterByPrice(min: Double, max: Double) {
        val filtered = originalIrons.filter { it.price in min..max }
        _irons.clear()
        _irons.addAll(filtered)
    }

    fun resetFilter() {
        _irons.clear()
        _irons.addAll(originalIrons)
    }

    /**
     * Добавляет новый IronModel в Excel-файл по Uri и в список.
     */
    fun addIron(newIron: IronModel, uri: Uri) {
        // Работа с файлом в фоне
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resolver = getApplication<Application>().contentResolver

                resolver.openInputStream(uri)?.use { inp ->
                    val workbook = XSSFWorkbook(inp)
                    val sheet = workbook.getSheetAt(0)

                    val newRow = sheet.createRow(sheet.lastRowNum + 1)
                    DataFormatter().let { fmt ->
                        newRow.createCell(0).setCellValue((sheet.lastRowNum+1).toDouble())  // №
                        newRow.createCell(1).setCellValue(newIron.model)
                        newRow.createCell(2).setCellValue(newIron.color)
                        newRow.createCell(3).setCellValue(newIron.power.toDouble())
                        newRow.createCell(4).setCellValue(newIron.soleType)
                        newRow.createCell(5).setCellValue(newIron.steamRate.toDouble())
                        newRow.createCell(6).setCellValue(if (newIron.steamBoost) "Есть" else "Нет")
                        newRow.createCell(7).setCellValue(if (newIron.steamControl) "Есть" else "Нет")
                        newRow.createCell(8).setCellValue(newIron.tankVolume.toDouble())
                        newRow.createCell(9).setCellValue(if (newIron.leakProtect) "Есть" else "Нет")
                        newRow.createCell(10).setCellValue(if (newIron.waterLevel) "Есть" else "Нет")
                        newRow.createCell(11).setCellValue(if (newIron.vertical) "Есть" else "Нет")
                        newRow.createCell(12).setCellValue(newIron.maxPressure)
                        newRow.createCell(13).setCellValue(if (newIron.antiScale) "Есть" else "Нет")
                        newRow.createCell(14).setCellValue(newIron.safety)
                        newRow.createCell(15).setCellValue(newIron.weight)
                        newRow.createCell(16).setCellValue(newIron.quantity.toDouble())
                        newRow.createCell(17).setCellValue(newIron.price)
                        newRow.createCell(18).setCellValue(newIron.quantity * newIron.price)
                    }

                    resolver.openOutputStream(uri, "rwt")?.use { out ->
                        workbook.write(out)
                    }
                    workbook.close()
                }

                viewModelScope.launch(Dispatchers.Main) {
                    originalIrons = originalIrons + newIron
                    _irons.add(newIron)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
