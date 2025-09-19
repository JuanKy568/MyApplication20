package com.example.myapplication

import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listView = findViewById<ListView>(R.id.listHistory)
        val lista = ArrayList<String>()

        // 🔹 Recuperamos el usuario registrado desde SharedPreferences
        val prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val userName = prefs.getString("USER_NAME", null)

        try {
            val admin = AdminSQLiteOpenHelper(this, "administracion", null, 1)
            val bd = admin.readableDatabase

            // 🔹 Filtramos solo los registros del usuario logueado
            val cursor = bd.rawQuery(
                "SELECT name, info, peso, altura, imc, date, time FROM imc WHERE name = ? COLLATE NOCASE",
                arrayOf(userName)
            )

            while (cursor.moveToNext()) {
                val name = cursor.getString(0)
                val info = cursor.getString(1)
                val peso = cursor.getString(2)
                val altura = cursor.getString(3)
                val imc = cursor.getString(4)
                val date = cursor.getString(5)
                val time = cursor.getString(6)

                lista.add("👤 $name \n $info (IMC: $imc)\n⚖️ Peso: $peso kg | 📏 Altura: $altura m\n📅 $date ⏰ $time")

            }

            Toast.makeText(this, "Registros de $userName: ${cursor.count}", Toast.LENGTH_SHORT).show()

            cursor.close()
            bd.close()
        } catch (e: SQLiteException) {
            Toast.makeText(this, "No se pudo cargar el historial: ${e.message}", Toast.LENGTH_LONG).show()
        }

        // Mostrar lista (aunque esté vacía si hubo error o no haya registros)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lista)
        listView.adapter = adapter
    }
}
