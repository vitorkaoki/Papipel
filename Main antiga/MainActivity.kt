//package com.example.papipel.Views
//
//import android.app.Activity
//import android.content.Intent
//import android.graphics.Color
//import android.os.Bundle
//import android.util.TypedValue
//import android.view.MenuItem
//import android.view.View
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.NumberPicker
//import android.widget.Toast
//import androidx.appcompat.app.ActionBarDrawerToggle
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.Toolbar
//import androidx.core.content.ContextCompat
//import androidx.core.view.GravityCompat
//import com.example.papipel.Adapter.OrderProductsAdapter
//import com.example.papipel.Adapter.ProductsByCategoryListAdapter
//import com.example.papipel.Database.DatabaseOrderProduct
//import com.example.papipel.Database.DatabaseOrders
//import com.example.papipel.Database.DatabaseProducts
//import com.example.papipel.Models.Order
//import com.example.papipel.Models.OrderProduct
//import com.example.papipel.Models.Product
//import com.example.papipel.R
//import com.example.papipel.R.layout
//import com.google.android.material.navigation.NavigationView
//import kotlinx.android.synthetic.main.activity_main.*
//import org.json.JSONObject
//import java.io.BufferedReader
//import java.io.InputStreamReader
//
//class MainActivity : AppCompatActivity(), View.OnClickListener,
//    NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {
//
//    private val requestCode = 1
//    private val databaseProducts = DatabaseProducts(this)
//    private val databaseOrders = DatabaseOrders(this)
//    private val databaseOrderProduct = DatabaseOrderProduct(this)
//    private var orderProductsHash = JSONObject()
//    var totalOrderPrice = 0.0
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(layout.activity_main)
//
//        // Set listeners
//        setButtonsListeners()
//
//        // Create the toolbar with the navigation drawer to have a menu
//        setSupportActionBar(toolbar as Toolbar?)
//        val togle = ActionBarDrawerToggle(this, drawer_layout,
//            toolbar as Toolbar?, R.string.nav_open_drawer, R.string.nav_close_drawer)
//        drawer_layout.addDrawerListener(togle)
//        togle.syncState()
//
//        // Make the menu clickable and deal with the click actions
//        nav_view.bringToFront()
//        nav_view.setNavigationItemSelectedListener(this)
//    }
//
//    override fun onStart() {
//        super.onStart()
//        // Load the spinner with the category
//        loadSpinnerCategories()
//
//        // Load the list of products for the first category on the list
//        if (spn_order_spinner_categories.selectedItem != null) {
//            inflateOrderProductsList(spn_order_spinner_categories.selectedItem.toString())
//        }
//    }
//
//    override fun onBackPressed() {
//
//        // Close just the navigation drawer when back button is pressed
//        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
//            drawer_layout.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
//    }
//
//    // Deal with the click on the navigation drawer buttons
//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//
//        if (item.itemId == R.id.update_products_list) {
//            startFileChooser()
//        } else if (item.itemId == R.id.activity_products_list) {
//            val intent = Intent(this, ProductsListActivity::class.java)
//            startActivity(intent)
//        } else if (item.itemId == R.id.activity_orders_list) {
//            val intent = Intent(this, OrdersListActivity::class.java)
//            startActivity(intent)
//        }
//
//        // Close the drawer after the button is clicked and unselect all the items
//        drawer_layout.closeDrawer(GravityCompat.START)
//        return true
//    }
//
//    // Load all the categories to put in the spinner
//    private fun loadSpinnerCategories() {
//        val categories = databaseProducts.getCategories()
//
//        if (!categories.isEmpty()) {
//            spn_order_spinner_categories.adapter = ArrayAdapter(this,
//                android.R.layout.simple_spinner_dropdown_item,
//                categories)
//            spn_order_spinner_categories.onItemSelectedListener = this
//        }
//    }
//
//    private fun setButtonsListeners() {
//        btn_close_order.setOnClickListener(this)
//        btn_add_items.setOnClickListener(this)
//        btn_see_order.setOnClickListener(this)
//    }
//
//    override fun onClick(v: View) {
//        val id = v.id
//        val typedValue = TypedValue()
//
//        // Deal with the click on the button of close an order
//        if (id == R.id.btn_close_order) {
//
//            // Open a dialog with the list of items of the order and the confirmation or not to
//            // close the order
//            closeOrder()
//        }
//        // See the view to add items to the order
//        else if (id == R.id.btn_add_items) {
//            theme.resolveAttribute(R.attr.selectableItemBackground, typedValue, true)
//
//            btn_add_items.background = ContextCompat.getDrawable(this, typedValue.resourceId)
//            btn_add_items.setTextColor(ContextCompat.getColor(this, R.color.thirdColor))
//
//            btn_see_order.background = ContextCompat.getDrawable(this, R.drawable.btn_bottom_left)
//            btn_see_order.setTextColor(Color.parseColor("#FFFFFF"))
//
//            // Show the elements for add an item to the order
//            lyt_select_category.visibility = View.VISIBLE
//            lstv_products_by_category_list.visibility = View.VISIBLE
//            lstv_order_products_list.visibility = View.GONE
//            lyt_total_order_price.visibility = View.GONE
//        }
//        // See the view to see the items for the order
//        else if (id == R.id.btn_see_order) {
//            theme.resolveAttribute(R.attr.selectableItemBackground, typedValue, true)
//
//            btn_see_order.background = ContextCompat.getDrawable(this, typedValue.resourceId)
//            btn_see_order.setTextColor(ContextCompat.getColor(this, R.color.thirdColor))
//
//            btn_add_items.background = ContextCompat.getDrawable(this, R.drawable.btn_bottom_right)
//            btn_add_items.setTextColor(Color.parseColor("#FFFFFF"))
//
//            // Hide the elements for add an item to the order
//            lyt_select_category.visibility = View.GONE
//            lstv_products_by_category_list.visibility = View.GONE
//            lstv_order_products_list.visibility = View.VISIBLE
//            lyt_total_order_price.visibility = View.VISIBLE
//        }
//    }
//
//    // Handle when nothing is selected in the spinner (will not be used)
//    override fun onNothingSelected(parent: AdapterView<*>?) {
//        TODO("Not yet implemented")
//    }
//
//    // Handle with an item selected in the spinner
//    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        val id = parent?.id
//
//        if (id == R.id.spn_order_spinner_categories) {
//            val category = parent?.getItemAtPosition(position).toString()
//            inflateOrderProductsList(category)
//        }
//    }
//
//    // Inflate the list of products with the first category
//    private fun inflateOrderProductsList(category: String) {
//
//        // Get the list of products by category from database
//        val products = databaseProducts.getProductByCategory(category)
//
//        // Inflate the listview with the products
//        val productsByCategoryListAdapter = ProductsByCategoryListAdapter(
//            this,
//            layout.products_by_category_list_row,
//            products
//        )
//        lstv_products_by_category_list.adapter = productsByCategoryListAdapter
//
//        // Handle with the item selected from the list
//        lstv_products_by_category_list.setOnItemClickListener { parent, view, position, id ->
//            val product = productsByCategoryListAdapter.getItem(position)
//            chooseQuantity(product)
//        }
//    }
//
//    // Open a dialog to choose the number of items for that product
//    private fun chooseQuantity(product: Product?) {
//        // Create a number picker
//        val numberPicker = NumberPicker(this)
//        numberPicker.maxValue = product!!.quantity
//        numberPicker.minValue = 1
//
//        // Build the alert dialog
//        val alertDialogBuilder = AlertDialog.Builder(this)
//            .setView(numberPicker)
//            .setTitle("Escolha a quantidade de items do pedido")
//
//        // With the confirmation, add the item in the list of items of the order
//        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
//            try {
//                var orderProduct = Product()
//                orderProduct.name = product.name
//                orderProduct.productId = product.productId
//                orderProduct.quantity = numberPicker.value
//                orderProduct.price = numberPicker.value * product.price
//                orderProductsHash.put(product.name, orderProduct)
//                inflateProductsOfOrder()
//                Toast.makeText(this, "Produto adicionado ao pedido", Toast.LENGTH_SHORT).show()
//            } catch (e: java.lang.Exception) {
//                Toast.makeText(this, "Erro ao adicionar o produto ao pedido", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        alertDialogBuilder.setNegativeButton("Cancelar") { dialog, which ->  }
//
//        //Show the dialog
//        alertDialogBuilder.show()
//    }
//
//    // Inflate the list of products in the order
//    private fun inflateProductsOfOrder() {
//
//        // Create the list with the products for the order
//        var products = mutableListOf<Product>()
//        val keys = orderProductsHash.keys()
//        keys.forEach { key ->
//            val product = orderProductsHash.get(key) as Product
//            products.add(product)
//            totalOrderPrice += product.price
//        }
//
//        // Inflate the listview with the products
//        val orderProductsListAdapter = OrderProductsAdapter(
//            this,
//            layout.order_products_list_row,
//            products
//        )
//        lstv_order_products_list.adapter = orderProductsListAdapter
//
//        // Handle with the item selected from the list
//        lstv_order_products_list.setOnItemClickListener { parent, view, position, id ->
//            val product = orderProductsListAdapter.getItem(position)
//            removeProductOrder(product)
//        }
//
//        // Set the total price of the order
//        var totalOrderPriceString = totalOrderPrice.toString().replace(".", ",")
//        total_order_price.text = "Total: R$ $totalOrderPriceString"
//    }
//
//    // Remove product for the order
//    private fun removeProductOrder(product: Product?) {
//        // Build the alert dialog
//        val alertDialogBuilder = AlertDialog.Builder(this)
//            .setTitle("Deseja remover o produto do pedido?")
//
//        // With the confirmation, add the item in the list of items of the order
//        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
//            orderProductsHash.remove(product?.name)
//            inflateProductsOfOrder()
//        }
//
//        alertDialogBuilder.setNegativeButton("Cancelar") { dialog, which ->  }
//
//        //Show the dialog
//        alertDialogBuilder.show()
//    }
//
//    // Open a dialog to confirm the close of the order and save the order in the database
//    private fun closeOrder() {
//
//        // Build the alert dialog
//        val alertDialogBuilder = AlertDialog.Builder(this)
//            .setTitle("Deseja concluir o pedido?")
//
//        // Set the positive button
//        alertDialogBuilder.setPositiveButton("Sim") { dialog, which ->
//
//            var resultInsertOrderProducts = false
//
//            // If the order is empty, do not do anything
//            if (totalOrderPrice > 0.0) {
//                // Insert the order in the database
//                var order = Order()
//                order.value = totalOrderPrice
//                val orderID = databaseOrders.insertOrder(order)
//
//                // Insert the products of the order in the table OrderProducts
//                if (orderID != -1.toLong()) {
//                    var orderProductsList = mutableListOf<OrderProduct>()
//                    orderProductsHash.keys().forEach { key ->
//                        val product = orderProductsHash.get(key) as Product
//                        val orderProduct = OrderProduct(orderID.toInt(), product.productId, product.quantity)
//                        orderProductsList.add(orderProduct)
//                    }
//                    resultInsertOrderProducts = databaseOrderProduct.insertOrderProductsList(orderProductsList)
//                }
//
//                // Update the quantity of products in the database
//                var products = mutableListOf<Product>()
//                orderProductsHash.keys().forEach { key ->
//                    products.add(orderProductsHash.get(key) as Product)
//                }
//                val resultUpdateQuantityProducts = databaseProducts.updateProductQuantity(products)
//
//                if ((orderID != -1.toLong()) && resultInsertOrderProducts && resultUpdateQuantityProducts) {
//                    // Clean the totalPrice of the order and the hash with the products of the order
//                    totalOrderPrice = 0.0
//                    orderProductsHash = JSONObject()
//
//                    // Reload all the data to create an order
//                    loadSpinnerCategories()
//                    inflateOrderProductsList(spn_order_spinner_categories.selectedItem.toString())
//                    inflateProductsOfOrder()
//
//                    Toast.makeText(this, "Pedido concluído", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "Erro ao concluir o pedido", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                Toast.makeText(this, "Pedido vazio", Toast.LENGTH_SHORT).show()
//            }
//
//        }
//
//        // Set the negative button
//        alertDialogBuilder.setNegativeButton("Não") { dialog, which -> }
//
//        //Show the dialog
//        alertDialogBuilder.show()
//    }
//
//    // Start the Intent with the file chooser
//    private fun startFileChooser() {
//        val intent = Intent()
//        intent.setAction(Intent.ACTION_OPEN_DOCUMENT)
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        intent.setType("*/*")
//        val mimeTypes = arrayOf("text/csv", "text/comma-separated-values")
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
//        intent.putExtra("android.content.extra.SHOW_ADVANCED", true)
//        startActivityForResult(intent, requestCode)
//
//        //        intent.setAction(Intent.ACTION_OPEN_DOCUMENT_TREE)
////        intent.putExtra("android.content.extra.SHOW_ADVANCED", true)
////        startActivityForResult(intent, 2)
//    }
//
//    // Override the function to deal with the file chosen
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (this.requestCode == requestCode && resultCode == Activity.RESULT_OK && data != null) {
//
//            // Transform csv file to product objects list
//            val products = csvToProductList(data)
//
//            // If no errors occurred inflate the list of products stored
//            if (products != null) {
//
//                // Populate the database of products
//                val result = databaseProducts.populateDatabase(products)
//                var message = ""
//                if (result) {
//                    message = "Banco de dados atualizado"
//                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//                } else {
//                    message = "Erro ao atualizar o banco de dados. Tente novamente"
//                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                val errorMessage = "Erro ao importar os arquivos. Tente novamente"
//                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    // Get the csv file and create a list of products with the data
//    private fun csvToProductList(data: Intent?) : MutableList<Product>? {
//
//        try {
//            // Read the csv file
//            var inputStream = contentResolver.openInputStream(data!!.data!!)
//            var reader = BufferedReader(InputStreamReader(inputStream)).lineSequence().iterator()
//
//            // Put the data from the csv file in a list of Products
//            var products = mutableListOf<Product>()
//
//            // Remove the first line that it is the header_navigation_drawer
//            reader.next()
//
//            // Get the products from the csv file and append in the list of products objects
//            reader.forEach { line ->
//                val product = line.split(",")
//                products.add(
//                    Product(
//                        product.get(0), product.get(1), product.get(2), product.get(3).toDouble(),
//                        product.get(4).toInt(), product.get(5), product.get(6).toInt()
//                    )
//                )
//            }
//            return products
//        } catch (e: Exception) {
//            return null
//        }
//    }
//}