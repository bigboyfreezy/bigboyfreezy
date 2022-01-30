package com.farah.notification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var name: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var submit1: MaterialButton
    private lateinit var mDbRef: DatabaseReference
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        mAuth = FirebaseAuth.getInstance()
        go_login.setOnClickListener {
            val i = Intent(applicationContext, SignUp::class.java)
            startActivity(i)}

        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        submit1 = findViewById(R.id.submit1)


        submit1.setOnClickListener {

            val email1 = email.text.toString()
            val password1= password.text.toString()
            val name1 = name.text.toString()
            signup(name1,email1,password1)
        }


    }
    fun signup(name1:String,email1:String, password1:String){
        mAuth.createUserWithEmailAndPassword(email1, password1)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    addUserToDatabase(name1,email1,mAuth.currentUser?.uid!!)
                    val i = Intent(applicationContext, MapsActivity::class.java)
                    startActivity(i)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(applicationContext, "Error"+task.exception, Toast.LENGTH_LONG).show()


                }
            }

    }
    fun addUserToDatabase(name1:String,email1:String,uid:String){
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(user(name1,email1,uid))

    }

}
