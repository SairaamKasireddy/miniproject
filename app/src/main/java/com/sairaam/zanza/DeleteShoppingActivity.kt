package com.sairaam.zanza

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import com.google.firebase.database.*

class DeleteShoppingActivity : AppCompatActivity() {

    //creating objects for ui components
    lateinit var listView:ListView
    lateinit var backButton: Button

    //creating data reference object
    lateinit var ref: DatabaseReference

    //creating objects for temporary variables
    lateinit var userId:String
    lateinit var shoppingItemsList:MutableList<ShoppingItems>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_shopping)

        //initializing ui components
        listView=findViewById(R.id.listView)
        backButton=findViewById(R.id.back_to_shopping_button)

        //initializing temporary variables
        shoppingItemsList= mutableListOf()
        userId=intent.getStringExtra("userId")

        //initializing database reference and adding value event listener
        ref= FirebaseDatabase.getInstance().getReference("users/$userId/shoppingList")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(ds: DataSnapshot) {
                if(ds.exists()){
                    shoppingItemsList.clear()
                    for(s in ds.children){
                        val shopitem=s.getValue(ShoppingItems::class.java)
                        shoppingItemsList.add(shopitem!!)
                    }
                    val adapter=ShoppingAdapter(applicationContext, R.layout.shopping_item,shoppingItemsList)
                    listView.adapter=adapter
                }
            }

        })

        //adding delete functionality to list items
        listView.onItemClickListener= AdapterView.OnItemClickListener{ parent, view, position, id->
            var selectedItem=shoppingItemsList[position]
            ref.child(position.toString()).removeValue()
        }

        //adding functionality to back button
        backButton.setOnClickListener {
            val shoppingActivity= Intent(this@DeleteShoppingActivity,ShoppingActivity::class.java)
            shoppingActivity.putExtra("userId",userId)
            startActivity(shoppingActivity)
        }
    }
}
