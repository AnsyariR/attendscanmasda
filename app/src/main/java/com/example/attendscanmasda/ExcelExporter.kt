package com.example.attendscanmasda

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ExcelExporter(private val context: Context) {
    fun exportToExcel(data: List<String>): File? {
        val fileName = "attendance_${System.currentTimeMillis()}.csv"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(storageDir, fileName)
        try {
            FileOutputStream(file).use { fos ->
                fos.write("Barcode\n".toByteArray())
                data.forEach {
                    fos.write((it + "\n").toByteArray())
                }
            }
            return file
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}
