package com.sairaam.zanza

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddShoppingAdapter(val mCtx: Context, val layoutResId: Int, val itemsList:MutableList<Items>,val userId:String)
    : ArrayAdapter<Items>(mCtx,layoutResId,itemsList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        //creating ui compenent objects
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View =layoutInflater.inflate(layoutResId,null)
        val mainItemName=view.findViewById<TextView>(R.id.main_item_name)
        val mainItemQuantity=view.findViewById<TextView>(R.id.main_item_quantity)
        val itemQuantityEditText=view.findViewById<EditText>(R.id.item_quantity_edittext)
        val addShoppingItemsButton=view.findViewById<Button>(R.id.add_item_to_shoplist_button)
        val item=itemsList[position]

        //database operations
        val ref= FirebaseDatabase.getInstance().getReference("users/$userId/shoppingList")

        //initializing objects
        mainItemName.text=item.itemName
        mainItemQuantity.text= "( ${item.itemCategory} )"
        mainItemName.isSingleLine=true

        //adding on click listener to add button
        addShoppingItemsButton.setOnClickListener {
            val shopItemId=ref.push().key
            if(itemQuantityEditText.text.toString()!="") {
                val shopItem = ShoppingItems(shopItemId.toString(), item, itemQuantityEditText.text.toString().trim().toDouble(), false)
                ref.child(position.toString()).setValue(shopItem).addOnCompleteListener {
                    Toast.makeText(mCtx, "item successfully saved", Toast.LENGTH_SHORT).show()
                    itemQuantityEditText.setText("")
                }
            }else{
                Toast.makeText(mCtx,"please enter item quantity",Toast.LENGTH_SHORT).show()
            }
        }
        return view;
    }

}