package com.example.snapchat_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity

class MainActivity : AppCompatActivity() {

    var snapchat_icon:ImageView?=null;
    var ConstraintLayout:ConstraintLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        snapchat_icon=findViewById(R.id.snapchat_icon)

    }

    fun signin(view: View){
        val intent= Intent(this,login_page::class.java)
        startActivity(intent)
        
    }
}

