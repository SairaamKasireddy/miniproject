package com.sairaam.zanza

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class InventoryAdapter(val mCtx: Context, val layoutResId: Int, val inventoryItemsList:MutableList<InventoryItems>)
    : ArrayAdapter<InventoryItems>(mCtx,layoutResId,inventoryItemsList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View =layoutInflater.inflate(layoutResId,null)
        val inventoryItemName=view.findViewById<TextView>(R.id.inventory_item_name)
        val inventoryItemQuantity=view.findViewById<TextView>(R.id.inventory_item_quantity)
        val inventoryItemCategory=view.findViewById<TextView>(R.id.inventory_item_category)
        val inventoryItem=inventoryItemsList[position]

        inventoryItemName.text=inventoryItem.item.itemName
        inventoryItemName.isSingleLine=true
        inventoryItemCategory.text=inventoryItem.item.itemCategory
        inventoryItemQuantity.text=inventoryItem.itemQuantity.toString()+" "+inventoryItem.item.weightType
        return view;
    }

}