package com.example.papipel.Models

import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
    var orderProductsHash = HashMap<String, Product>()
    var totalOrderPrice = 0.0
    lateinit var productsStockList: MutableList<Product>
}