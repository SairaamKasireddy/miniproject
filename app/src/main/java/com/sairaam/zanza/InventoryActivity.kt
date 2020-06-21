package com.sairaam.zanza

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*

class InventoryActivity : AppCompatActivity() {

    //creating ui objects
    lateinit var listView: ListView

    //creating database reference variable
    lateinit var ref:DatabaseReference

    //creating objects for temporary variables
    lateinit var inventoryItemsList:MutableList<InventoryItems>
    lateinit var userId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        //initializing ui components
        listView=findViewById(R.id.listView)

        //initializing temporary variables
        inventoryItemsList= mutableListOf()
        userId=intent.getStringExtra("userId")

        //initializing database refernce
        ref=FirebaseDatabase.getInstance().getReference("users/$userId/inventory")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    inventoryItemsList.clear()
                    for(i in p0.children){
                        val inventoryItem=i.getValue(InventoryItems::class.java)
                        if (inventoryItem != null) {
                            if(inventoryItem.item.itemId!="") {
                                inventoryItemsList.add(inventoryItem!!)
                            }
                        }
                    }
                    if(inventoryItemsList.isEmpty()){
                        Toast.makeText(applicationContext,"No items in inventory", Toast.LENGTH_LONG).show()
                    }else {
                        val adapter = InventoryAdapter(applicationContext, R.layout.inventory_item, inventoryItemsList)
                        listView.adapter = adapter
                    }
                }
            }
        })
    }
}
