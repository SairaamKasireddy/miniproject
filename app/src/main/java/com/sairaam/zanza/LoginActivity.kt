package com.sairaam.zanza

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    //creating objects for ui components
    lateinit var usernameEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var loginButton: Button
    lateinit var signupButton: Button

    //creating database reference object
    lateinit var ref: DatabaseReference

    //creating temporary variables for usage
    lateinit var userList:MutableList<Users>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //initializing ui components
        usernameEditText=findViewById(R.id.login_username_edittext)
        passwordEditText=findViewById(R.id.login_password_edittext)
        loginButton=findViewById(R.id.login_button)
        signupButton=findViewById(R.id.signup_button)

        //initiating temporary variables
        userList= mutableListOf()

        //initializing database reference and adding value listener
        ref= FirebaseDatabase.getInstance().getReference("users")
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

        Toast.makeText(applicationContext,"please enter your details",Toast.LENGTH_LONG).show()
        //adding onClickListener for login button
        loginButton.setOnClickListener {
            login()
        }

        //adding onClickListener for sign up button
        signupButton.setOnClickListener {

            //redirecting user to sign up page
            val signupActivity= Intent(this@LoginActivity,SignupActivity::class.java)
            startActivity(signupActivity)
        }
    }

    //function for logging in the user
    private fun login() {

        //getting values from the edit texts
        val username=usernameEditText.text.toString().trim()
        val password=passwordEditText.text.toString().trim()

        //checking if field is empty
        if(username==""||password==""){
            Toast.makeText(applicationContext,"please enter your details",Toast.LENGTH_LONG).show()
        }

        //verifying user details
        var usernameFound:Boolean=false
        for(u in userList){
            if(u.userName==username){
                if(u.password==password){

                    //redirecting to main activity page
                    usernameFound=true
                    val mainActivity= Intent(this@LoginActivity,MainActivity::class.java)
                    mainActivity.putExtra("userId",u.userId)
                    startActivity(mainActivity)
                }else{

                    //show error message
                    Toast.makeText(applicationContext,"Incorrect password",Toast.LENGTH_LONG).show()
                    return
                }
            }
        }
        if(!usernameFound) {
            Toast.makeText(applicationContext, "Incorrect username", Toast.LENGTH_LONG).show()
        }
    }
}
