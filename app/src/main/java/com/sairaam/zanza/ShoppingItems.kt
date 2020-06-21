package com.sairaam.zanza

class ShoppingItems(val shopItemId:String, val item:Items, val itemQuantity:Double, val striked:Boolean) {
    constructor():this("",Items("","","",""),0.0,false){}
}