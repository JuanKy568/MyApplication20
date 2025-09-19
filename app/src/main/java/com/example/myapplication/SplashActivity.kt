package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) // tu layout del splash

        // Acceder a SharedPreferences
        val prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val savedName = prefs.getString("USER_NAME", null)

        // Espera 2 segundos y decide a dónde ir
        Handler(Looper.getMainLooper()).postDelayed({
            if (savedName.isNullOrEmpty()) {
                // Primera vez -> pedir nombre
                val intent = Intent(this, NameActivity::class.java)
                startActivity(intent)
            } else {
                // Ya existe un nombre -> ir directo al MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 2000)
    }
}
