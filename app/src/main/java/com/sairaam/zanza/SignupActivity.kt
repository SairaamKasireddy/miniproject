package com.sairaam.zanza

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*

class SignupActivity : AppCompatActivity() {

    //creating objects for ui components
    lateinit var usernameEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var phoneEditText: EditText
    lateinit var signupButton: Button

    //creating object for database reference
    lateinit var ref:DatabaseReference

    //creating objects for temporary variables
    lateinit var userList:MutableList<Users>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //initializing ui components
        usernameEditText=findViewById(R.id.signup_username_edittext)
        passwordEditText=findViewById(R.id.signup_password_edittext)
        phoneEditText=findViewById(R.id.signup_phone_edittext)
        signupButton=findViewById(R.id.signup_button)

        //initializing database reference
        ref= FirebaseDatabase.getInstance().getReference("users")

        //initiating temporary variables
        userList= mutableListOf()

        //adding value listener to database reference
        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(de: DatabaseError) {
            }
            override fun onDataChange(ds: DataSnapshot) {
                if(ds.exists()){
                    userList.clear()
                    for(u in ds.children){
                        val user=u.getValue(Users::class.java)
                        userList.add(user!!)
                    }
                }
            }
        })

        //adding onClickListener for sign up button
        signupButton.setOnClickListener {
            signup()
        }

    }

    private fun signup() {

        //getting values from edit texts
        val username=usernameEditText.text.toString().trim()
        val password=passwordEditText.text.toString().trim()
        val phone=phoneEditText.text.toString().toLong()

        //checking for empty fields
        if(username==""||password==""||phone==0.toLong()){
            Toast.makeText(applicationContext,"Please enter your details",Toast.LENGTH_LONG).show()
            return
        }

        //checking if the username already exists
        for(u in userList){
            if(u.userName==username){
                if(u.phoneNumber==phone){

                    //displaying a message that user already exists and redirecting to login activity
                    Toast.makeText(applicationContext,"User already Exists",Toast.LENGTH_LONG).show()
                }else{

                    //display a message saying username already exists and chose another name
                    Toast.makeText(applicationContext,"Username already exists choose a different username",Toast.LENGTH_LONG).show()
                    usernameEditText.isFocusable=true
                }
            }else{

                //creating and adding new user to database
                val userid=ref.push().key
                val user=Users(userid.toString(), username, password,phone,
                    listOf<ShoppingItems>(ShoppingItems("",Items(),0.0,false)), listOf<InventoryItems>())
                ref.child(userid.toString()).setValue(user).addOnCompleteListener {
                    Toast.makeText(applicationContext, "signup successful", Toast.LENGTH_LONG).show()
                    usernameEditText.setText("")
                    passwordEditText.setText("")
                    phoneEditText.setText("")
                }
                val mainActivity= Intent(this@SignupActivity,MainActivity::class.java)
                mainActivity.putExtra("userId",userid.toString())
                startActivity(mainActivity)
            }
        }

    }
}
