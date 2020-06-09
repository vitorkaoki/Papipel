package com.example.papipel.Views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.example.papipel.Adapter.ProductsListAdapter
import com.example.papipel.Database.DatabaseProducts
import com.example.papipel.Models.Product
import com.example.papipel.R
import com.example.papipel.R.layout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener {

    private val requestCode = 1
    private lateinit var path: String
    private val databaseProducts = DatabaseProducts(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        // Set listeners
        setButtonsListeners()

        // Create the toolbar with the navigation drawer to have a menu
        setSupportActionBar(toolbar as Toolbar?)
        val togle = ActionBarDrawerToggle(this, drawer_layout,
            toolbar as Toolbar?, R.string.nav_open_drawer, R.string.nav_close_drawer)
        drawer_layout.addDrawerListener(togle)
        togle.syncState()

        // Make the menu clickable and deal with the click actions
        nav_view.bringToFront()
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {

        // Close just the navigation drawer when back button is pressed
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // Deal with the click on the navigation drawer buttons
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.update_products_list) {
            startFileChooser()
        } else if (item.itemId == R.id.activity_products_list) {
            val intent = Intent(this, ProductsListActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.activity_orders_list) {
            Toast.makeText(this, "Em breve", Toast.LENGTH_SHORT).show()
        }

        // Close the drawer after the button is clicked and unselect all the items
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setButtonsListeners() {
    }

    override fun onClick(v: View) {
        val id = v.id
    }

    // Start the Intent with the file chooser
    private fun startFileChooser() {
        val intent = Intent()
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setType("*/*")
        val mimeTypes = arrayOf("text/csv", "text/comma-separated-values")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.putExtra("android.content.extra.SHOW_ADVANCED", true)
        startActivityForResult(intent, requestCode)

        //        intent.setAction(Intent.ACTION_OPEN_DOCUMENT_TREE)
//        intent.putExtra("android.content.extra.SHOW_ADVANCED", true)
//        startActivityForResult(intent, 2)
    }

    // Override the function to deal with the file chosen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (this.requestCode == requestCode && resultCode == Activity.RESULT_OK && data != null) {

            // Transform csv file to product objects list
            val products = csvToProductList(data)

            // If no errors occurred inflate the list of products stored
            if (products != null) {

                // Populate the database of products
                val result = databaseProducts.populateDatabase(products)
                var message = ""
                if (result) {
                    message = "Banco de dados atualizado"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                } else {
                    message = "Erro ao atualizar o banco de dados. Tente novamente"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            } else {
                val errorMessage = "Erro ao importar os arquivos. Tente novamente"
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Get the csv file and create a list of products with the data
    private fun csvToProductList(data: Intent?) : MutableList<Product>? {

        try {
            // Read the csv file
            var inputStream = contentResolver.openInputStream(data!!.data!!)
            var reader = BufferedReader(InputStreamReader(inputStream)).lineSequence().iterator()

            // Put the data from the csv file in a list of Products
            var products = mutableListOf<Product>()

            // Remove the first line that it is the header_navigation_drawer
            reader.next()

            // Get the products from the csv file and append in the list of products objects
            reader.forEach { line ->
                val product = line.split(",")
                products.add(
                    Product(
                        product.get(0), product.get(1), product.get(2), product.get(3).toDouble(),
                        product.get(4).toInt(), product.get(5)
                    )
                )
            }
            return products
        } catch (e: Exception) {
            return null
        }
    }
}