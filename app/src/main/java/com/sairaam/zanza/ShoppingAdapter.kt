package com.sairaam.zanza

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ShoppingAdapter(val mCtx: Context, val layoutResId: Int, val shoppingList:MutableList<ShoppingItems>)
    : ArrayAdapter<ShoppingItems>(mCtx,layoutResId,shoppingList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View =layoutInflater.inflate(layoutResId,null)
        val shopItemName=view.findViewById<TextView>(R.id.shopItemName)
        val shopItemQuantity=view.findViewById<TextView>(R.id.shopItemQuantity)
        val shoppingItems=shoppingList[position]

        shopItemName.text=shoppingItems.item.itemName
        shopItemName.isSingleLine=true
        if(shoppingItems.striked) {
            shopItemName.paintFlags = shopItemName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        shopItemQuantity.text="quantity: "+shoppingItems.itemQuantity+" "+shoppingItems.item.weightType
        return view;
    }

}