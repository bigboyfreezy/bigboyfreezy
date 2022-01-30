package com.farah.notification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var submit: MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()

        go_register.setOnClickListener {
            val i = Intent(applicationContext, SignUp::class.java)
            startActivity(i)}
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        submit = findViewById(R.id.submit)


        submit.setOnClickListener {

            val email1 = email.text.toString()
            val password1= password.text.toString()
            signin(email1,password1)
        }



    }
    fun signin(email1:String, password1:String){
        mAuth.signInWithEmailAndPassword(email1, password1)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val i = Intent(applicationContext, MapsActivity::class.java)
                    startActivity(i)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
                }
            }



    }
}