package com.sairaam.zanza

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.database.*

class MainItemsActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    //creating objects for ui components
    lateinit var itemNameEditText:EditText
    lateinit var itemCategorySpinner: Spinner
    lateinit var itemWeighSpinner: Spinner
    lateinit var addItemsButton: Button

    //creating database reference object
    lateinit var ref:DatabaseReference

    //creating objects for temporary variables
    lateinit var itemName:String
    lateinit var itemCategory:String
    lateinit var itemWeightType:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_items)

        //initializing ui components
        itemNameEditText=findViewById(R.id.item_name_edittext)
        itemCategorySpinner=findViewById(R.id.category_spinner)
        itemWeighSpinner=findViewById(R.id.weight_spinner)
        addItemsButton=findViewById(R.id.add_items_button)

        //initializing database reference
        ref= FirebaseDatabase.getInstance().getReference("items")

        //adding on click listener for add items button
        addItemsButton.setOnClickListener {
            addItem()
        }

        //adding values for weight spinner
        ArrayAdapter.createFromResource(this, R.array.items_weight_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                itemWeighSpinner.adapter = adapter
            }
        itemWeighSpinner.onItemSelectedListener = this

        //adding values for category spinner
        ArrayAdapter.createFromResource(this, R.array.items_category_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                itemCategorySpinner.adapter = adapter
            }
        itemCategorySpinner.onItemSelectedListener = this
    }

    //function to add items into database
    private fun addItem() {

        //getting values from ui components
        itemName=itemNameEditText.text.toString().trim()

        //creating a random unique key for item
        val itemId=ref.push().key

        //creating item object and pushing into database
        val item = Items(itemId.toString(), itemName, itemCategory, itemWeightType)
        ref.child(itemId.toString()).setValue(item).addOnCompleteListener {
            Toast.makeText(applicationContext, "Item saved successfully", Toast.LENGTH_LONG).show()
            itemNameEditText.setText("")
        }
    }

    //spinner methods
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        //getting selected item value
        if(parent==itemCategorySpinner){
            itemCategory=parent!!.getItemAtPosition(position).toString()
        }
        if(parent==itemWeighSpinner){
            itemWeightType=parent!!.getItemAtPosition(position).toString()
        }

    }

}
