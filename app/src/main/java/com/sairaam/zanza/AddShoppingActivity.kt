package com.sairaam.zanza

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*
import com.mancj.materialsearchbar.MaterialSearchBar

class AddShoppingActivity : AppCompatActivity() {

    //creating objects for ui components
    lateinit var listView:ListView
    lateinit var searchBar: MaterialSearchBar

    //creating data reference objects
    lateinit var ref1:DatabaseReference
    lateinit var ref2:DatabaseReference

    //creating objects for temporary variables
    lateinit var userId:String
    lateinit var itemsList:MutableList<Items>
    lateinit var adapter:ArrayAdapter<Items>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_shopping)

        //initializing ui components
        listView=findViewById(R.id.listView)
        searchBar=findViewById(R.id.searchBar)

        //initializing temporary variables
        userId=intent.getStringExtra("userId")
        itemsList= mutableListOf()

        //initializing data base reference and adding value listener
        ref1= FirebaseDatabase.getInstance().getReference("items")
        ref2= FirebaseDatabase.getInstance().getReference("users/$userId/shoppingList")
        ref1.addValueEventListener(object: ValueEventListener {
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
                    adapter=AddShoppingAdapter(applicationContext, R.layout.add_shopping_list_item, itemsList,userId)
                    listView.adapter=adapter
                }
            }
        })

        searchBar.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                //SEARCH FILTER
                adapter.getFilter().filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })

    }
}
