package com.example.tiendamovil.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiendamovil.Prefs
import com.example.tiendamovil.databinding.ActivityProductsBinding
import com.example.tiendamovil.model.CartCreateRequest
import com.example.tiendamovil.model.Product
import com.example.tiendamovil.net.RetrofitClient
import kotlinx.coroutines.*

class ProductsActivity : AppCompatActivity() {
    private lateinit var b: ActivityProductsBinding
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val items = mutableListOf<Product>()
    private lateinit var adapter: ProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(b.root)

        // Si no hay token, regresa a login
        if (Prefs(this).getToken() == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Adapter requiere onAdd: (Product) -> Unit
        adapter = ProductsAdapter(items) { product ->
            scope.launch {
                try {
                    val api = RetrofitClient.api(this@ProductsActivity)
                    val res = withContext(Dispatchers.IO) {
                        api.addToCart(CartCreateRequest(product.id, 1))
                    }
                    Toast.makeText(this@ProductsActivity, res.message, Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@ProductsActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        b.rvProducts.layoutManager = LinearLayoutManager(this)
        b.rvProducts.adapter = adapter

        b.btnLogout.setOnClickListener {
            scope.launch {
                try {
                    val api = RetrofitClient.api(this@ProductsActivity)
                    withContext(Dispatchers.IO) { api.logout() }
                } catch (_: Exception) {}
                Prefs(this@ProductsActivity).clear()
                startActivity(Intent(this@ProductsActivity, LoginActivity::class.java))
                finish()
            }
        }

        b.btnCart.setOnClickListener {
            startActivity(Intent(this@ProductsActivity, CartActivity::class.java))
        }

        cargarProductos()
    }

    private fun cargarProductos() {
        scope.launch {
            try {
                val api = RetrofitClient.api(this@ProductsActivity)
                val data = withContext(Dispatchers.IO) { api.products() } // ← método correcto
                items.clear()
                items.addAll(data)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(this@ProductsActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
