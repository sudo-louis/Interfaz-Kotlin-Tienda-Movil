package com.example.tiendamovil.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tiendamovil.databinding.ItemProductBinding
import com.example.tiendamovil.model.Product

class ProductsAdapter(
    private val data: List<Product>,
    private val onAdd: (Product) -> Unit
) : RecyclerView.Adapter<ProductsAdapter.VH>() {

    inner class VH(val b: ItemProductBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val p = data[position]
        holder.b.tvName.text = p.name
        holder.b.tvPrice.text = "$${p.price}"

        val url = p.image
        if (!url.isNullOrBlank()) {
            Glide.with(holder.b.img.context)
                .load(url) // ya viene absoluta del backend
                .centerCrop()
                .into(holder.b.img)
        } else {
            holder.b.img.setImageDrawable(null)
        }

        holder.b.btnAdd.setOnClickListener { onAdd(p) }
    }

    override fun getItemCount() = data.size
}
