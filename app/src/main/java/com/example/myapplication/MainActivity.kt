package com.example.myapplication

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    // Conectamos el ViewModel usando el Factory que creamos antes
    private val inventarioViewModel: InventarioViewModel by viewModels {
        InventarioViewModelFactory((application as InventarioApp).database.productoDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar la lista (RecyclerView)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = ProductoAdapter { productoSeleccionado ->
            // Al hacer clic en un producto, abrimos el diálogo de movimientos
            mostrarDialogoMovimiento(productoSeleccionado)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Observar los datos: Si cambia la base de datos, se actualiza la lista sola
        inventarioViewModel.todosLosProductos.observe(this) { productos ->
            adapter.submitList(productos)
        }

        // Botón flotante para agregar producto nuevo
        val fab = findViewById<FloatingActionButton>(R.id.fabAgregar)
        fab.setOnClickListener {
            mostrarDialogoNuevoProducto()
        }
    }

    // ---------------------------------------------------------
    // DIÁLOGO 1: CONTROL DE STOCK (Lo que pediste: Entradas y Salidas)
    // ---------------------------------------------------------
    private fun mostrarDialogoMovimiento(producto: Producto) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Gestionar ${producto.nombre}")
        builder.setMessage("Stock actual: ${producto.cantidad} ${producto.unidad}\n\n¿Cuánto ingresa o sale?")

        // Campo para escribir la cantidad
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        input.hint = "Cantidad (Ej: 5.0)"
        builder.setView(input)

        // Botón "Ingresar" (Suma)
        builder.setPositiveButton("INGRESAR (+)") { _, _ ->
            val cantidad = input.text.toString().toDoubleOrNull()
            if (cantidad != null) {
                inventarioViewModel.registrarMovimiento(producto, cantidad, esEntrada = true)
                Toast.makeText(this, "Stock actualizado", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón "Retirar" (Resta)
        builder.setNegativeButton("RETIRAR (-)") { _, _ ->
            val cantidad = input.text.toString().toDoubleOrNull()
            if (cantidad != null) {
                // Validación simple para no quedar en negativo
                if (producto.cantidad >= cantidad) {
                    inventarioViewModel.registrarMovimiento(producto, cantidad, esEntrada = false)
                    Toast.makeText(this, "Stock descontado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "¡Error! No hay suficiente stock", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Botón Cancelar
        builder.setNeutralButton("Cancelar", null)

        builder.show()
    }

    // ---------------------------------------------------------
    // DIÁLOGO 2: CREAR PRODUCTO NUEVO (Solo la primera vez)
    // ---------------------------------------------------------
    private fun mostrarDialogoNuevoProducto() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Nuevo Producto")

        // Creamos un diseño vertical para poner varios campos
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val inputNombre = EditText(this)
        inputNombre.hint = "Nombre (Ej: Pollo)"
        layout.addView(inputNombre)

        val inputCategoria = EditText(this)
        inputCategoria.hint = "Categoría (Ej: Carnes)"
        layout.addView(inputCategoria)

        val inputUnidad = EditText(this)
        inputUnidad.hint = "Unidad (Ej: kg, paquetes)"
        layout.addView(inputUnidad)

        val inputCantidad = EditText(this)
        inputCantidad.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        inputCantidad.hint = "Stock Inicial (Ej: 0)"
        layout.addView(inputCantidad)

        builder.setView(layout)

        builder.setPositiveButton("Guardar") { _, _ ->
            val nombre = inputNombre.text.toString()
            val categoria = inputCategoria.text.toString()
            val unidad = inputUnidad.text.toString()
            val cantidad = inputCantidad.text.toString().toDoubleOrNull() ?: 0.0

            if (nombre.isNotEmpty()) {
                val nuevoProducto = Producto(0, nombre, categoria, cantidad, unidad)
                inventarioViewModel.insertar(nuevoProducto)
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }
}