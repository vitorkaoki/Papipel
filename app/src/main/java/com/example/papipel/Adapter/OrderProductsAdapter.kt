package com.example.papipel.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.papipel.Models.Product
import com.example.papipel.R
import org.json.JSONObject

class OrderProductsAdapter (
var mContext: Context,
var resource: Int,
var products: MutableList<Product>) : ArrayAdapter<Product>(mContext, resource, products) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(mContext)
        val view = layoutInflater.inflate(resource, null)

        // Get all the text views in the order product row
        val productNameView = view.findViewById<TextView>(R.id.txtv_order_product_name)
        val productQuantityView = view.findViewById<TextView>(R.id.txtv_order_product_quantity)

        // Set the text views with the values from the product object
        var product = products.get(position)
        productNameView.text = product.name
        productQuantityView.text =
            product.quantity.toString() + if (product.quantity == 1) " unidade" else " unidades"

        return view
    }
}