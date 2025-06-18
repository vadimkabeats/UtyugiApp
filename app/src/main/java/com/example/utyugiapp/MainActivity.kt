package com.example.utyugiapp

import android.app.Activity
import android.content.Intent
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class MainActivity : ComponentActivity() {
    private var excelUri: Uri? = null
    private lateinit var excelPickerLauncher: ActivityResultLauncher<Intent>
    private val viewModel: MainViewModel by viewModels()
    private val PREFS_NAME = "utyugi_prefs"
    private val KEY_URI    = "excel_uri"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.getString(KEY_URI, null)?.let { uriString ->
            try {
                val uri = Uri.parse(uriString)
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                excelUri = uri

                loadIronsFromExcel(uri)
            } catch (e: Exception) {

                prefs.edit().remove(KEY_URI).apply()
            }
        }

        excelPickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    excelUri = uri
                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    prefs.edit().putString(KEY_URI, uri.toString()).apply()
                    loadIronsFromExcel(uri)
                }
            }
        }

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(
                            irons               = viewModel.irons,
                            onSortPriceAsc      = { viewModel.sortByPriceAsc() },
                            onSortPriceDesc     = { viewModel.sortByPriceDesc() },
                            onSortModelAsc      = { viewModel.sortByModelAsc() },
                            onSortModelDesc     = { viewModel.sortByModelDesc() },
                            onFilterClick       = { min, max -> viewModel.filterByPrice(min, max) },
                            onResetFilterClick  = { viewModel.resetFilter() },
                            onAddClick          = { navController.navigate("add") },
                            onRefreshClick      = { pickExcelFile() },
                            onItemClick         = { iron -> navController.navigate("details/${iron.number}") }
                        )
                    }
                    composable("details/{number}") { backStack ->
                        val num = backStack.arguments?.getString("number")?.toIntOrNull()
                        viewModel.irons.find { it.number == num }?.let {
                            DetailScreen(it)
                        }
                    }
                    composable("add") {
                        AddIronScreen(
                            onSave = { newIron ->
                                excelUri?.let { uri ->
                                    viewModel.addIron(newIron, uri)
                                    loadIronsFromExcel(uri)
                                }
                                navController.popBackStack()
                            },
                            onCancel = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }

    private fun pickExcelFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            )
        }
        excelPickerLauncher.launch(intent)
    }

    private fun loadIronsFromExcel(uri: Uri) {
        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val workbook  = XSSFWorkbook(inputStream)
                val sheet     = workbook.getSheetAt(0)
                val formatter = DataFormatter()

                val irons = mutableListOf<IronModel>()
                for (r in 1..sheet.lastRowNum) {
                    val row = sheet.getRow(r) ?: continue
                    val values = (0..17).map { idx ->
                        formatter.formatCellValue(row.getCell(idx))
                    }
                    val numStr    = values.getOrNull(0) ?: ""
                    val modelStr  = values.getOrNull(1) ?: ""
                    val colorStr  = values.getOrNull(2) ?: ""
                    val powerStr  = values.getOrNull(3) ?: ""
                    val soleStr   = values.getOrNull(4) ?: ""
                    val steamRate = values.getOrNull(5) ?: ""
                    val boostStr  = values.getOrNull(6) ?: ""
                    val ctrlStr   = values.getOrNull(7) ?: ""
                    val tankStr   = values.getOrNull(8) ?: ""
                    val leakStr   = values.getOrNull(9) ?: ""
                    val waterStr  = values.getOrNull(10) ?: ""
                    val vertStr   = values.getOrNull(11) ?: ""
                    val pressStr  = values.getOrNull(12) ?: ""
                    val scaleStr  = values.getOrNull(13) ?: ""
                    val safetyStr = values.getOrNull(14) ?: ""
                    val weightStr = values.getOrNull(15) ?: ""
                    val qtyStr    = values.getOrNull(16) ?: ""
                    val priceStr  = values.getOrNull(17) ?: ""

                    irons.add(
                        IronModel(
                            number       = numStr.toIntOrNull() ?: 0,
                            model        = modelStr,
                            color        = colorStr,
                            power        = powerStr.toIntOrNull() ?: 0,
                            soleType     = soleStr,
                            steamRate    = steamRate.toIntOrNull() ?: 0,
                            steamBoost   = boostStr.equals("Есть", true),
                            steamControl = ctrlStr.equals("Есть", true),
                            tankVolume   = tankStr.toIntOrNull() ?: 0,
                            leakProtect  = leakStr.equals("Есть", true),
                            waterLevel   = waterStr.equals("Есть", true),
                            vertical     = vertStr.equals("Есть", true),
                            maxPressure  = pressStr.replace(',', '.').toDoubleOrNull() ?: 0.0,
                            antiScale    = scaleStr.equals("Есть", true),
                            safety       = safetyStr,
                            weight       = weightStr.replace(',', '.').toDoubleOrNull() ?: 0.0,
                            quantity     = qtyStr.toIntOrNull() ?: 0,
                            price        = priceStr.replace(',', '.').toDoubleOrNull() ?: 0.0
                        )
                    )
                }
                workbook.close()
                viewModel.setIrons(irons)
            } ?: throw IllegalStateException("Не удалось открыть InputStream")
        } catch (e: Exception) {
            Log.e("UtyugiApp", "Ошибка при чтении Excel", e)
            Toast.makeText(this, "Не удалось загрузить таблицу: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }
}
