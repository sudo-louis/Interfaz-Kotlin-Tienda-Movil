package com.example.tiendamovil.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiendamovil.databinding.ActivityCartBinding
import com.example.tiendamovil.model.CartItem
import com.example.tiendamovil.net.RetrofitClient
import kotlinx.coroutines.*

class CartActivity : AppCompatActivity() {
    private lateinit var b: ActivityCartBinding
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val items = mutableListOf<CartItem>()
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCartBinding.inflate(layoutInflater)
        setContentView(b.root)

        adapter = CartAdapter(items)
        b.rvCart.layoutManager = LinearLayoutManager(this)
        b.rvCart.adapter = adapter

        cargarCarrito()
    }

    private fun cargarCarrito() {
        scope.launch {
            try {
                val api = RetrofitClient.api(this@CartActivity)
                val data = withContext(Dispatchers.IO) { api.getCart() }
                items.clear()
                items.addAll(data)
                adapter.notifyDataSetChanged()
                val total = data.sumOf { it.subtotal ?: 0.0 }
                b.tvTotal.text = "Total: $${"%.2f".format(total)}"
            } catch (e: Exception) {
                Toast.makeText(this@CartActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
