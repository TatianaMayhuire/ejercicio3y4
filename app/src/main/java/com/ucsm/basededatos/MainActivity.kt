package com.ucsm.basededatos

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ucsm.basededatos.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: ArticuloRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = AppDatabase.getInstance(this).articuloDao()
        repository = ArticuloRepository(dao)

        observarArticulos()

        binding.btnRegistrar.setOnClickListener { registrar() }
        binding.btnBuscar.setOnClickListener { buscar() }
        binding.btnModificar.setOnClickListener { modificar() }
        binding.btnEliminar.setOnClickListener { eliminar() }
    }

    private fun toast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun limpiarCampos() {
        binding.txtCodigo.setText("")
        binding.txtDescripcion.setText("")
        binding.txtPrecio.setText("")
    }

    private fun observarArticulos() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                repository.articulos.collect { lista ->
                    val texto = lista.joinToString("\n") {
                        "${it.codigo} - ${it.descripcion} - S/ ${it.precio}"
                    }

                    binding.txtLista.text = texto
                }
            }
        }
    }

    private fun registrar() {
        val codigo = binding.txtCodigo.text.toString()
        val descripcion = binding.txtDescripcion.text.toString()
        val precio = binding.txtPrecio.text.toString()

        if (codigo.isEmpty() || descripcion.isEmpty() || precio.isEmpty()) {
            toast("Debe llenar todos los campos")
            return
        }

        val articulo = Articulo(
            codigo.toInt(),
            descripcion,
            precio.toDouble()
        )

        lifecycleScope.launch {
            try {
                repository.insertar(articulo)
                limpiarCampos()
                toast("Registro exitoso")
            } catch (e: Exception) {
                toast("Error: ${e.message}")
            }
        }
    }

    private fun buscar() {
        val codigo = binding.txtCodigo.text.toString()

        if (codigo.isEmpty()) {
            toast("Ingrese un código")
            return
        }

        lifecycleScope.launch {
            val articulo = repository.buscarPorCodigo(codigo.toInt())

            if (articulo != null) {
                binding.txtDescripcion.setText(articulo.descripcion)
                binding.txtPrecio.setText(articulo.precio.toString())
            } else {
                toast("No existe")
            }
        }
    }

    private fun eliminar() {
        val codigo = binding.txtCodigo.text.toString()

        if (codigo.isEmpty()) {
            toast("Ingrese un código")
            return
        }

        lifecycleScope.launch {
            val eliminado = repository.eliminarPorCodigo(codigo.toInt())
            limpiarCampos()

            if (eliminado == 1) {
                toast("Eliminado correctamente")
            } else {
                toast("No existe")
            }
        }
    }

    private fun modificar() {
        val codigo = binding.txtCodigo.text.toString()
        val descripcion = binding.txtDescripcion.text.toString()
        val precio = binding.txtPrecio.text.toString()

        if (codigo.isEmpty() || descripcion.isEmpty() || precio.isEmpty()) {
            toast("Complete todos los campos")
            return
        }

        val articulo = Articulo(
            codigo.toInt(),
            descripcion,
            precio.toDouble()
        )

        lifecycleScope.launch {
            val actualizado = repository.actualizar(articulo)

            if (actualizado == 1) {
                toast("Actualizado correctamente")
            } else {
                toast("No existe")
            }
        }
    }
}