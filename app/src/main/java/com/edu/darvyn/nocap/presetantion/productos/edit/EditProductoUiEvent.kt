package com.edu.darvyn.nocap.presetantion.productos.edit

sealed interface EditProductoUiEvent {
    data class LoadProducto(val id: Int) : EditProductoUiEvent
    data class NombreChange(val nombre: String) : EditProductoUiEvent
    data class DescripcionChange(val descripcion: String) : EditProductoUiEvent
    data class PrecioCompraChange(val precioCompra: String) : EditProductoUiEvent
    data class PrecioVentaChange(val precio: String) : EditProductoUiEvent
    data class ImagenChange(val url: String) : EditProductoUiEvent
    data class StockChange(val stock: String) : EditProductoUiEvent
    data class CategoriaChange(val categoriaId: Int) : EditProductoUiEvent
    data class MarcaChange(val marcaId: Int) : EditProductoUiEvent
    data class ToggleTamano(val tamano: String) : EditProductoUiEvent
    data class ToggleColor(val color: String) : EditProductoUiEvent
    object Save : EditProductoUiEvent
    object LoadCategorias: EditProductoUiEvent
    object LoadMarcas: EditProductoUiEvent

}