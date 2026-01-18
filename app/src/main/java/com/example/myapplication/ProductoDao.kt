package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductoDao {
    @Query("SELECT * FROM tabla_congeladora ORDER BY nombre ASC")
    fun obtenerTodos(): Flow<List<Producto>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar (producto: Producto)

    @Query("Update tabla_congeladora SET cantidad = :nuevaCantidad WHERE id = :productoId")
    suspend fun actualizarStock(productoId: Int, nuevaCantidad: Double)

    @Delete
    suspend fun eliminar(producto: Producto)



}