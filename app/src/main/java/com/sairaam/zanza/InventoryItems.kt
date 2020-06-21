package com.sairaam.zanza

class InventoryItems( val itemId:String, val item:Items, val itemQuantity:Double, val minQuantity:Double) {
    constructor():this("",Items("","","",""),0.0,0.0){}
}