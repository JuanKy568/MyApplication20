package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // ajuste de bordes
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val userName = prefs.getString("USER_NAME", null)

        // datos
        val pesoEditText = findViewById<EditText>(R.id.peso)
        val alturaEditText = findViewById<EditText>(R.id.altura)
        val calcularBtn = findViewById<Button>(R.id.btn)
        val historyBtn = findViewById<Button>(R.id.btnHistory)
        val backBtn = findViewById<Button>(R.id.btnBack)
        val resultadoTextView = findViewById<TextView>(R.id.tv1)
        val name = findViewById<TextView>(R.id.txtname)

        name.text = getString(R.string.usuario_txt) + userName

        // boton calcular
        calcularBtn.setOnClickListener {
            val pesoStr = pesoEditText.text.toString()
            val alturaStr = alturaEditText.text.toString()

            if (pesoStr.isNotEmpty() && alturaStr.isNotEmpty()) {
                val peso = pesoStr.toDouble()
                val altura = alturaStr.toDouble()

                if (altura > 0) {
                    val imc = peso / (altura * altura)

                    val categoria = when {
                        imc < 18.5 -> getString(R.string.imc1)
                        imc < 24.9 -> getString(R.string.imc2)
                        imc < 29.9 -> getString(R.string.imc3)
                        else -> getString(R.string.imc4)
                    }

                    resultadoTextView.text = "IMC: %.2f\n$categoria".format(imc)
                    savename(imc, categoria)

                } else {
                    resultadoTextView.text = getString(R.string.mensaje1)
                }
            } else {
                resultadoTextView.text = getString(R.string.mensaje2)
            }
        }

        // ver historial
        historyBtn.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        // regresar
        backBtn.setOnClickListener {
            // borrar name
            prefs.edit().remove("SAVE_NAME").apply()

            // volver a name
            val intent = Intent(this, NameActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // funcion para guardar imc y nombre
    private fun savename(imc: Double, categoria: String) {
        val prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val userName = prefs.getString("USER_NAME", null)

        // fecha
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        // hora
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val currentTime = timeFormat.format(Date())

        // registro
        val admin = AdminSQLiteOpenHelper(this, "administracion", null, 1)
        val bd = admin.writableDatabase
        val registro = ContentValues()
        registro.put("name", userName)
        registro.put("info", categoria)
        registro.put("imc", imc)
        registro.put("date", currentDate)
        registro.put("time", currentTime)
        bd.insert("imc", null, registro)
        bd.close()
        Toast.makeText(this, getString(R.string.mensaje3), Toast.LENGTH_SHORT).show()
    }
}
