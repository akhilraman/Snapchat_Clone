package com.example.snapchat_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class login_page : AppCompatActivity() {

    var emailEditText:EditText?=null
    val mauth=FirebaseAuth.getInstance()

    var passwordEditText:EditText?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        emailEditText=findViewById(R.id.emailEditText)
        passwordEditText=findViewById(R.id.passwordEditText)

        if(mauth.currentUser!=null){
            login()
        }else{

        }

    }

    fun go(view: View){
        var email=emailEditText?.text.toString()
        var password=passwordEditText?.text.toString()
        //Signup the user here
        mauth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    login();
                } else {
                    // If sign in fails, register the user automatically
                    mauth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                var uniq_id=task.result.toString()
                                FirebaseDatabase.getInstance().getReference().child("users").child(task.result?.user!!.uid).child("email").setValue(email)

                                login()

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(this, "Failed try again", Toast.LENGTH_SHORT).show()
                            }
                        }


                }
            }

    }

    fun login(){
        //go to snap page
        val intent=Intent(this,chat_page::class.java)
        startActivity(intent)
        //Toast.makeText(this, "DONE", Toast.LENGTH_SHORT).show()
    }



}