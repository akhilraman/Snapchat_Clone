package com.example.snapchat_clone

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.math.log


class snap_page : AppCompatActivity() {
    var snapimageview:ImageView?=null
    val imageName=UUID.randomUUID().toString()+".jpg"

    var captioneditText:EditText?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snap_page)

        snapimageview=findViewById(R.id.snap_image)
        captioneditText=findViewById(R.id.captioneditText)
    }

    fun getPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto()
            }
        }
    }


    fun upload(view: View){
        getPhoto()
    }

    fun send_snap(view:View){

        snapimageview?.isDrawingCacheEnabled = true
        snapimageview?.buildDrawingCache()
        val bitmap = (snapimageview?.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = FirebaseStorage.getInstance().getReference().child("images").child(imageName).putBytes(data)
        uploadTask.addOnFailureListener {
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            val result = taskSnapshot.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener { uri ->
                var photoStringLink = uri.toString()
                var intent=Intent(this,ChooseUser::class.java)
                intent.putExtra("imageURL",photoStringLink)
                intent.putExtra("imageName",imageName)
                intent.putExtra("message",captioneditText?.text.toString())
                startActivity(intent)
                println("here")
                }
            //intent.putExtra("imageURL",photoStringLink)
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            // ...
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val selectedImage = data!!.data
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            try {
               val bitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,selectedImage)
                snapimageview?.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}

