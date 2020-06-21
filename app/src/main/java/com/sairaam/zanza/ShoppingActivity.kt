package com.sairaam.zanza

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*

class ShoppingActivity : AppCompatActivity() {

    //creating objects for ui components
    lateinit var addShoppingItemsButton:Button
    lateinit var deleteShoppingItemsButton:Button
    lateinit var checkoutButton: Button
    lateinit var listView:ListView

    //creating database reference objects
    lateinit var ref1:DatabaseReference
    lateinit var ref2:DatabaseReference
    lateinit var ref3:DatabaseReference

    //creating objects for temporary use
    lateinit var shoppingItemsList:MutableList<ShoppingItems>
    lateinit var inventoryItemsList:MutableList<InventoryItems>
    lateinit var itemsList:MutableList<Items>
    lateinit var userId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping)

        //initializing ui objects
        addShoppingItemsButton=findViewById(R.id.add_shopping_items_button)
        deleteShoppingItemsButton=findViewById(R.id.delete_shopping_items_button)
        checkoutButton=findViewById(R.id.checkout_button)
        listView=findViewById(R.id.listView)

        //initiating temporary objects
        shoppingItemsList= mutableListOf()
        userId=intent.getStringExtra("userId")
        inventoryItemsList= mutableListOf()
        itemsList= mutableListOf()

        //initializing database reference
        ref1= FirebaseDatabase.getInstance().getReference("users/$userId/shoppingList")
        ref2= FirebaseDatabase.getInstance().getReference("users/$userId/inventory")
        ref3= FirebaseDatabase.getInstance().getReference("items")
        ref1.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    shoppingItemsList.clear()
                    for(r in p0.children){
                        val shopItem=r.getValue(ShoppingItems::class.java)
                        if (shopItem != null) {
                            if(shopItem.shopItemId!="") {
                                shoppingItemsList.add(shopItem!!)
                            }
                        }
                    }
                    if(shoppingItemsList.isEmpty()){
                        Toast.makeText(applicationContext,"No items in cart",Toast.LENGTH_LONG).show()
                    }else {
                        val adapter = ShoppingAdapter(applicationContext, R.layout.shopping_item, shoppingItemsList)
                        listView.adapter = adapter
                    }
                }
            }
        })
        ref2.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    inventoryItemsList.clear()
                    for(i in p0.children){
                        val inventoryItem=i.getValue(InventoryItems::class.java)
                        if (inventoryItem != null) {
                            if(inventoryItem.itemId!="") {
                                inventoryItemsList.add(inventoryItem!!)
                            }
                        }
                    }
                }
            }
        })
        ref3.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    itemsList.clear()
                    for(i in p0.children){
                        val item=i.getValue(Items::class.java)
                        if (item != null) {
                            if(item.itemId!="") {
                                itemsList.add(item!!)
                            }
                        }
                    }
                }
            }
        })

        //adding on click listener to add shopping items button
        addShoppingItemsButton.setOnClickListener {
            val addShoppingActivity= Intent(this@ShoppingActivity,AddShoppingActivity::class.java)
            addShoppingActivity.putExtra("userId",userId)
            startActivity(addShoppingActivity)
        }

        //adding on click listener to add shopping items button
        deleteShoppingItemsButton.setOnClickListener {
            val deleteShoppingActivity= Intent(this@ShoppingActivity,DeleteShoppingActivity::class.java)
            deleteShoppingActivity.putExtra("userId",userId)
            startActivity(deleteShoppingActivity)
        }

        //adding striking function
        listView.onItemClickListener=AdapterView.OnItemClickListener{ parent,view,position,id->
            var selectedItem=shoppingItemsList[position]
            var itemPosition=0
            for(i in itemsList){
                if(i.itemId==selectedItem.item.itemId){
                    break
                }
                itemPosition++
            }
            ref1.child(itemPosition.toString()).child("striked").setValue((selectedItem.striked xor true))
        }

        //adding on click listener to checkout button
        checkoutButton.setOnClickListener {
            var position:Int=0
            var itemListPosition:Int=0
            var existsInInventory:Boolean=false
            for(s in shoppingItemsList){
                if(s.striked){
                    existsInInventory=false
                    itemListPosition=0
                    for(i in inventoryItemsList){
                        if(i.item.itemId==s.item.itemId){
                            updateItem(s,itemListPosition,i)
                            existsInInventory=true
                            ref1.child(itemListPosition.toString()).removeValue()
                            break
                        }
                        itemListPosition++
                    }
                    var itemPosition=0
                    if(!existsInInventory) {
                        for(i in itemsList){
                            if(i.itemId==s.item.itemId){
                                break
                            }
                            itemPosition++
                        }
                        addItem(s, itemPosition)
                        ref1.child(itemPosition.toString()).removeValue()
                    }
                }
                position++
            }
            finish();
            startActivity(intent);
        }
    }

    //update quantity of item if it exists
    private fun updateItem(s: ShoppingItems, position: Int, i: InventoryItems) {
        var newQuantity=s.itemQuantity+i.itemQuantity
        val newItem=InventoryItems(i.itemId,i.item,newQuantity,0.0)
        ref2.child(position.toString()).setValue(newItem)
    }

    //adding items into inventory if they dont exist
    private fun addItem(s: ShoppingItems,itemPosition:Int) {
        val itemId=ref2.push().key
        val inventoryItem=InventoryItems(itemId.toString(),s.item,s.itemQuantity,0.0)
        ref2.child(itemPosition.toString()).setValue(inventoryItem)
        return
    }
}
