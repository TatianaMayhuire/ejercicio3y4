package com.ucsm.basededatos

import kotlinx.coroutines.flow.Flow

class ArticuloRepository(
    private val dao: ArticuloDao
) {
    val articulos: Flow<List<Articulo>> = dao.listarTodos()

    suspend fun insertar(articulo: Articulo) {
        dao.insertar(articulo)
    }

    suspend fun buscarPorCodigo(codigo: Int): Articulo? {
        return dao.buscarPorCodigo(codigo)
    }

    suspend fun actualizar(articulo: Articulo): Int {
        return dao.actualizar(articulo)
    }

    suspend fun eliminarPorCodigo(codigo: Int): Int {
        return dao.eliminarPorCodigo(codigo)
    }
}