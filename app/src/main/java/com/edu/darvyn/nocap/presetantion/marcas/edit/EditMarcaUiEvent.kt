package com.edu.darvyn.nocap.presetantion.marcas.edit


sealed interface EditMarcaUiEvent {
    data class LoadMarca (val id: Int) : EditMarcaUiEvent
    data class MarcaNombreChange(val nombre: String) : EditMarcaUiEvent
    data object Save: EditMarcaUiEvent
}