package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historial_movimientos")
data class Movimiento(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombreProducto: String, // Guardamos el nombre por si borran el producto original
    val cantidad: Double,
    val esEntrada: Boolean,     // true = "Entró", false = "Salió"
    val fecha: Long = System.currentTimeMillis() // Guarda la hora exacta automáticamente
)