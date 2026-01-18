package com.example.myapplication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

// Esta clase recibe una función 'onProductoClick' para saber qué hacer cuando tocas un producto
class ProductoAdapter(private val onProductoClick: (Producto) -> Unit) :
    ListAdapter<Producto, ProductoAdapter.ProductoViewHolder>(ProductoComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        // Aquí "inflamos" (creamos) el diseño de la tarjeta que hiciste antes
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val productoActual = getItem(position)
        holder.bind(productoActual)
    }

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtNombre: TextView = itemView.findViewById(R.id.txtNombre)
        private val txtCategoria: TextView = itemView.findViewById(R.id.txtCategoria)
        private val txtCantidad: TextView = itemView.findViewById(R.id.txtCantidad)

        fun bind(producto: Producto) {
            txtNombre.text = producto.nombre
            txtCategoria.text = producto.categoria
            // Mostramos la cantidad con su unidad (Ej: "19.0 kg")
            txtCantidad.text = "${producto.cantidad} ${producto.unidad}"

            // LÓGICA VISUAL: Si queda poco stock (menos de 5), ponerlo en ROJO
            if (producto.cantidad < 5.0) {
                txtCantidad.setTextColor(Color.RED)
            } else {
                txtCantidad.setTextColor(Color.parseColor("#2E7D32")) // Verde oscuro
            }

            // Detectar el clic en la tarjeta
            itemView.setOnClickListener {
                onProductoClick(producto)
            }
        }
    }

    // Esto ayuda a la lista a saber si un producto cambió para actualizar solo ese y no toda la pantalla
    class ProductoComparator : DiffUtil.ItemCallback<Producto>() {
        override fun areItemsTheSame(oldItem: Producto, newItem: Producto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Producto, newItem: Producto): Boolean {
            return oldItem == newItem
        }
    }
}