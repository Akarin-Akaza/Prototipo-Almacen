package com.example.myapplication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MovimientoAdapter : ListAdapter<Movimiento, MovimientoAdapter.MovimientoViewHolder>(MovimientoComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovimientoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movimiento, parent, false)
        return MovimientoViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovimientoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MovimientoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtProducto: TextView = itemView.findViewById(R.id.txtProducto)
        private val txtFecha: TextView = itemView.findViewById(R.id.txtFecha)
        private val txtCantidad: TextView = itemView.findViewById(R.id.txtCantidadMov)
        private val imgIcono: ImageView = itemView.findViewById(R.id.imgIcono)

        fun bind(movimiento: Movimiento) {
            txtProducto.text = movimiento.nombreProducto

            // Formatear la fecha (Ej: 18/01/2026 15:30)
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            txtFecha.text = sdf.format(Date(movimiento.fecha))

            if (movimiento.esEntrada) {
                txtCantidad.text = "+${movimiento.cantidad}"
                txtCantidad.setTextColor(Color.parseColor("#2E7D32")) // Verde
                imgIcono.setImageResource(android.R.drawable.arrow_up_float)
                imgIcono.setColorFilter(Color.parseColor("#2E7D32"))
            } else {
                txtCantidad.text = "-${movimiento.cantidad}"
                txtCantidad.setTextColor(Color.parseColor("#C62828")) // Rojo
                imgIcono.setImageResource(android.R.drawable.arrow_down_float)
                imgIcono.setColorFilter(Color.parseColor("#C62828"))
            }
        }
    }

    class MovimientoComparator : DiffUtil.ItemCallback<Movimiento>() {
        override fun areItemsTheSame(oldItem: Movimiento, newItem: Movimiento) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movimiento, newItem: Movimiento) = oldItem == newItem
    }
}