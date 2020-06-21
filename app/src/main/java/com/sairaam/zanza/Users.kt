package com.sairaam.zanza

class Users(val userId:String, val userName:String,val password:String, val phoneNumber:Long, val shoppingList:List<ShoppingItems>
,val inventory:List<InventoryItems>) {
    constructor():this("","","",0.toLong(), listOf(), listOf())
}
