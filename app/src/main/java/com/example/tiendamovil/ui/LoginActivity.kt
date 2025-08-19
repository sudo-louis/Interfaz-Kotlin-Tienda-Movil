package com.example.tiendamovil.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tiendamovil.Prefs
import com.example.tiendamovil.databinding.ActivityLoginBinding
import com.example.tiendamovil.model.LoginRequest
import com.example.tiendamovil.net.RetrofitClient
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {
    private lateinit var b: ActivityLoginBinding
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)

        if (Prefs(this).getToken() != null) {
            startActivity(Intent(this, ProductsActivity::class.java))
            finish()
            return
        }

        b.btnLogin.setOnClickListener {
            val email = b.etEmail.text.toString().trim()
            val pass = b.etPass.text.toString().trim()

            scope.launch {
                try {
                    val api = RetrofitClient.api(this@LoginActivity)
                    val res = withContext(Dispatchers.IO) { api.login(LoginRequest(email, pass)) }
                    Prefs(this@LoginActivity).saveToken(res.token)
                    Toast.makeText(this@LoginActivity, "Bienvenido ${res.user.name}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, ProductsActivity::class.java))
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        b.btnToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
