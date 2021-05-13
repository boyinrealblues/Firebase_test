package com.example.informationfirtest

import android.icu.text.IDNA
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.informationfirtest.databinding.ActivityEntryPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

data class Information(var name:String="",var job:String="",var anime:String="")

class EntryPage : AppCompatActivity(),ValueEventListener {
    private val TAG = "EntryPage"
    lateinit private var UserID:String
    lateinit  private var binding:ActivityEntryPageBinding
    lateinit private var mAuth:FirebaseAuth
    lateinit private  var mAuthListener:FirebaseAuth.AuthStateListener
    lateinit private var mDatabase:FirebaseDatabase
    lateinit private var mReference:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_page)
        
        binding= DataBindingUtil.setContentView(this,R.layout.activity_entry_page)
        mAuth=Firebase.auth
        mDatabase= FirebaseDatabase.getInstance()
        mReference=mDatabase.reference
        mAuthListener=FirebaseAuth.AuthStateListener {
            val user=mAuth.currentUser
            if(user!=null) {
                UserID=user.uid
                Log.d(TAG, "${user.email} SIGNED IN")
                toastMessage("${user.email} SIGNED IN ")
            }else {
                Log.d(TAG, "SIGNED OUT")
                toastMessage("SIGNED OUT")
            }
        }
        mReference.addValueEventListener(this)
        
    }
    override fun onStop() {
        super.onStop()
        mAuth.addAuthStateListener(mAuthListener)
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAuthListener)
    }
    fun toastMessage(Str:String){
        Toast.makeText(this,Str, Toast.LENGTH_SHORT).show()
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        updateUI(snapshot)
    }

    private fun updateUI(snapshot: DataSnapshot) {

            val info=Information()
            Log.e(TAG,"${snapshot.child(UserID)}")
            info.name=snapshot.child(UserID).getValue(Information::class.java)!!.name
            info.job=snapshot.child(UserID).getValue(Information::class.java)!!.job
            info.anime=snapshot.child(UserID).getValue(Information::class.java)!!.anime

            Log.d(TAG,"name retrieved "+info.name)
            Log.d(TAG,"job retrieved "+info.job)
            Log.d(TAG,"Favorite anime retrieved "+info.anime)
            val adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,listOf(info.name,info.job,info.anime))
            binding.listView.adapter=adapter
        }


    override fun onCancelled(error: DatabaseError) {
        toastMessage("Database ERROR")
         }

}