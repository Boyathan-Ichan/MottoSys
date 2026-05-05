package com.technologiesdesesperation.mottosys.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    val userEmail = MutableStateFlow<String?>(null)
    val hasHandheld = MutableStateFlow(false)

    private val _currentCart = MutableStateFlow<List<ProductItem>>(emptyList())
    val currentCart: StateFlow<List<ProductItem>> = _currentCart

    // Para XML
    val currentCartLiveData = _currentCart.asLiveData()

    fun addToCart(product: ProductItem) {
        _currentCart.value = _currentCart.value + product
    }

    fun clearCart() {
        _currentCart.value = emptyList()
    }
}