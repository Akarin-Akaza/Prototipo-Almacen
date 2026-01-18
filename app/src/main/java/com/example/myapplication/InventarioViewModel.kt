package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class InventarioViewModel(private val repository: ProductoDao) : ViewModel() {

    val todosLosProductos: LiveData<List<Producto>> = repository.obtenerTodos().asLiveData()

    fun insertar(producto: Producto) = viewModelScope.launch {
        repository.insertar(producto)
    }

    fun registrarMovimiento(producto: Producto, cantidadMovimiento: Double, esEntrada: Boolean) {
        viewModelScope.launch {
            var nuevaCantidad = producto.cantidad

            if (esEntrada) {
                nuevaCantidad += cantidadMovimiento
            } else {
                if (nuevaCantidad >= cantidadMovimiento) {
                    nuevaCantidad -= cantidadMovimiento
                } else {
                    return@launch // Si no alcanza, no hacemos nada
                }
            }
            repository.actualizarStock(producto.id, nuevaCantidad)
            val movimiento = Movimiento(
                nombreProducto = producto.nombre,
                cantidad = cantidadMovimiento,
                esEntrada = esEntrada
            )
            repository.insertarMovimiento(movimiento)
        }
    }
    val todoElHistorial: LiveData<List<Movimiento>> = repository.obtenerHistorial().asLiveData()
}

class InventarioViewModelFactory(private val repository: ProductoDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventarioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventarioViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}