package com.example.tiendamovil.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tiendamovil.databinding.ItemCartBinding
import com.example.tiendamovil.model.CartItem

class CartAdapter(private val data: List<CartItem>)
    : RecyclerView.Adapter<CartAdapter.VH>() {

    inner class VH(val b: ItemCartBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val it = data[position]
        holder.b.tvName.text = it.name ?: "Producto"
        holder.b.tvInfo.text = "x${it.quantity}  •  $${String.format("%.2f", it.price ?: 0.0)}  →  $${String.format("%.2f", it.subtotal ?: 0.0)}"
        val url = it.image
        if (!url.isNullOrBlank()) {
            Glide.with(holder.b.img.context).load(url).centerCrop().into(holder.b.img)
        } else holder.b.img.setImageDrawable(null)
    }

    override fun getItemCount() = data.size
}
