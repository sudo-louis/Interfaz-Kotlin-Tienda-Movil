package com.example.tiendamovil.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tiendamovil.databinding.ActivityRegisterBinding
import com.example.tiendamovil.model.RegisterRequest
import com.example.tiendamovil.net.RetrofitClient
import kotlinx.coroutines.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var b: ActivityRegisterBinding
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.btnRegister.setOnClickListener {
            val name = b.etName.text.toString().trim()
            val email = b.etEmail.text.toString().trim()
            val pass = b.etPass.text.toString().trim()

            scope.launch {
                // ...
                scope.launch {
                    try {
                        val api = RetrofitClient.api(this@RegisterActivity)
                        val res = withContext(Dispatchers.IO) {
                            api.register(RegisterRequest(name, email, pass))
                        }
                        Toast.makeText(this@RegisterActivity, res.message, Toast.LENGTH_SHORT).show()
                        finish()
                    } catch (e: retrofit2.HttpException) {
                        val err = e.response()?.errorBody()?.string()
                        Toast.makeText(this@RegisterActivity, "HTTP ${e.code()} ${err}", Toast.LENGTH_LONG).show()
                    } catch (e: java.io.IOException) {
                        Toast.makeText(this@RegisterActivity, "Network error: ${e.message}", Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
