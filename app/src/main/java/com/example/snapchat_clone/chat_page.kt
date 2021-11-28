package com.example.snapchat_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class chat_page : AppCompatActivity() {
    val mauth= FirebaseAuth.getInstance()
    var chatlistview:ListView?=null
    var emails:ArrayList<String> = ArrayList()

    var snaps:ArrayList<DataSnapshot> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_page)
        chatlistview=findViewById(R.id.chatlistview)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, emails)
        chatlistview?.adapter = adapter
        var presentuser=mauth.currentUser!!.uid
        Toast.makeText(this, presentuser, Toast.LENGTH_SHORT).show()

        FirebaseDatabase.getInstance().getReference().child("users").child(presentuser).child("snaps").addChildEventListener(object:ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var email=snapshot.child("from").value.toString()
                emails.add(email)
                snaps.add(snapshot)
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

                var index=0
                for(snap:DataSnapshot in snaps){
                    if(snap.key==snapshot.key){
                        snaps.removeAt(index)
                        emails.removeAt(index)
                    }
                    index++
                }
                adapter.notifyDataSetChanged()

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        chatlistview?.onItemClickListener=
            AdapterView.OnItemClickListener{ adpterView, view, i, l->
            val snapshot=snaps.get(i)
            val intent= Intent(this,final_snap::class.java)
            intent.putExtra("imageName",snapshot.child("imageName").value.toString())
            Toast.makeText(this, snapshot.child("imageName").value.toString(), Toast.LENGTH_SHORT).show()
            intent.putExtra("imageURL",snapshot.child("imageURL").value.toString())
            intent.putExtra("message",snapshot.child("message").value.toString())
            intent.putExtra("snapKey",snapshot.key.toString())
            startActivity(intent)
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater=menuInflater
        inflater.inflate(R.menu.snaps,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item?.itemId==R.id.createsnap){
                val intent=Intent(this,snap_page::class.java)
                startActivity(intent)

        }else if(item?.itemId==R.id.logout){
            mauth.signOut()
            finish()

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        mauth.signOut()
        finish()
        super.onBackPressed()
    }


}