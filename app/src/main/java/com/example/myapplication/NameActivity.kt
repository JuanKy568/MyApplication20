package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class NameActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)

        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // datos UI
        val txtName = findViewById<EditText>(R.id.txtName)   // correo
        val txtPass = findViewById<EditText>(R.id.txtPass)   // contraseña
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegistro = findViewById<Button>(R.id.btnRegistro)

        // 🔹 BOTÓN REGISTRARSE
        btnRegistro.setOnClickListener {
            val email = txtName.text.toString().trim()
            val password = txtPass.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Guardar en SharedPreferences
                            val prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                            prefs.edit().putString("USER_NAME", email).apply()

                            Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()

                            // Ir a MainActivity
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                if (email.isEmpty()) txtName.error = "Ingrese un correo"
                if (password.isEmpty()) txtPass.error = "Ingrese una contraseña"
            }
        }

        // 🔹 BOTÓN LOGIN
        btnLogin.setOnClickListener {
            val email = txtName.text.toString().trim()
            val password = txtPass.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Ingreso exitoso", Toast.LENGTH_SHORT).show()

                            // Guardar correo en SharedPreferences
                            val prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                            prefs.edit().putString("USER_NAME", email).apply()

                            // Ir a MainActivity
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                if (email.isEmpty()) txtName.error = "Ingrese un correo"
                if (password.isEmpty()) txtPass.error = "Ingrese una contraseña"
            }
        }
    }
}
