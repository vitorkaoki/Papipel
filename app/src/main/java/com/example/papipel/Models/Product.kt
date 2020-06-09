package com.example.papipel.Models

class Product {
    var id = ""
    var name = ""
    var category = ""
    var price = 0.0
    var quantity = 0
    var description = ""

    constructor()
    constructor(id: String, name: String, category: String, price: Double, quantity: Int,
                description: String) {
        this.id = id
        this.name = name
        this.category = category
        this.price = price
        this.quantity = quantity
        this.description = description
    }
}