package com.example.snapchat_clone

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.net.HttpURLConnection
import java.net.URL


class final_snap : AppCompatActivity() {

    var snapimageview:ImageView?=null
    val mauth= FirebaseAuth.getInstance()
    var captionview:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_snap)

        snapimageview=findViewById(R.id.snapimageview)
        captionview=findViewById(R.id.captionview)

        captionview?.text=intent.getStringExtra("message")

        downloadImage(snapimageview)

    }
    fun downloadImage(view: View?) {
        val task = ImageDownloader()
        val myImage: Bitmap
        try {
            myImage =
                task.execute(intent.getStringExtra("imageURL") as String)
                    .get()!!
            snapimageview?.setImageBitmap(myImage)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    inner class ImageDownloader : AsyncTask<String, Void, Bitmap>() {
         override fun doInBackground(vararg urls: String): Bitmap? {
            return try {
                val url = URL(urls[0])
                val connection =
                    url.openConnection() as HttpURLConnection
                connection.connect()
                val `in` = connection.inputStream
                BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        var child1=intent.getStringExtra("snapKey").toString()
        var child2=intent.getStringExtra("imageName").toString()
        var currentuser=mauth.currentUser?.uid
        println(currentuser)
        println(child1)
        println(child2)
        FirebaseDatabase.getInstance().getReference().child("users").child(currentuser.toString()).child("snaps").child(child1).removeValue()

        FirebaseStorage.getInstance().getReference().child("images").child(child2).delete()

    }


}



