package com.example.attendscanmasda

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResultLauncher

class MainActivity : AppCompatActivity() {
    private val attendanceList = mutableListOf<String>()
    private lateinit var adapter: AttendanceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btnScan = findViewById<Button>(R.id.btnScan)
        val btnExport = findViewById<Button>(R.id.btnExport)
        val rvAttendance = findViewById<RecyclerView>(R.id.rvAttendance)
        val tvAttendanceList = findViewById<TextView>(R.id.tvAttendanceList)

        adapter = AttendanceAdapter(attendanceList)
        rvAttendance.adapter = adapter

        val scanLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val barcode = result.data?.getStringExtra("barcode")
                if (barcode != null) {
                    attendanceList.add(barcode)
                    adapter.notifyItemInserted(attendanceList.size - 1)
                }
            }
        }

        btnScan.setOnClickListener {
            val intent = Intent(this, BarcodeScannerActivity::class.java)
            scanLauncher.launch(intent)
        }

        btnExport.setOnClickListener {
            if (attendanceList.isEmpty()) {
                Toast.makeText(this, "Belum ada data kehadiran", Toast.LENGTH_SHORT).show()
            } else {
                val exporter = ExcelExporter(this)
                val file = exporter.exportToExcel(attendanceList)
                if (file != null) {
                    Toast.makeText(this, "File disimpan di: ${file.absolutePath}", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Gagal menyimpan file", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
