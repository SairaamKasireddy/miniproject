package com.sairaam.zanza

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    //creating objects for ui components
    lateinit var mainItemsButton: Button
    lateinit var shoppingButton: Button
    lateinit var inventoryButton:Button
    lateinit var userId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initializing ui components
        mainItemsButton=findViewById(R.id.add_root_items)
        shoppingButton=findViewById(R.id.shopping_button)
        userId=intent.getStringExtra("userId")
        inventoryButton=findViewById(R.id.inventory_button)

        //adding on click listener to main items button
        mainItemsButton.setOnClickListener {
            val mainItemsActivity= Intent(this@MainActivity,MainItemsActivity::class.java)
            startActivity(mainItemsActivity)
        }

        //adding on click listener to shopping button
        shoppingButton.setOnClickListener {
            val shoppingActivity= Intent(this@MainActivity,ShoppingActivity::class.java)
            shoppingActivity.putExtra("userId",userId)
            startActivity(shoppingActivity)
        }

        //adding on click listener to inventory button
        inventoryButton.setOnClickListener {
            val inventoryActivity= Intent(this@MainActivity,InventoryActivity::class.java)
            inventoryActivity.putExtra("userId",userId)
            startActivity(inventoryActivity)
        }
    }
}
