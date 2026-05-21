package com.ucsm.basededatos

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticuloDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(articulo: Articulo)

    @Update
    suspend fun actualizar(articulo: Articulo): Int

    @Query("DELETE FROM articulos WHERE codigo = :codigo")
    suspend fun eliminarPorCodigo(codigo: Int): Int

    @Query("SELECT * FROM articulos WHERE codigo = :codigo LIMIT 1")
    suspend fun buscarPorCodigo(codigo: Int): Articulo?

    @Query("SELECT * FROM articulos ORDER BY codigo ASC")
    fun listarTodos(): Flow<List<Articulo>>
}