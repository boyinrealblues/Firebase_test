package com.example.informationfirtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.informationfirtest.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.security.KeyStore

class MainActivity : AppCompatActivity() {

    private val TAG = ".MainActivity"

    lateinit private var binding:ActivityMainBinding
    lateinit private var mAuth:FirebaseAuth
    lateinit private var mAuthListener:FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        mAuth = Firebase.auth

        mAuthListener=FirebaseAuth.AuthStateListener {
            val user=mAuth.currentUser
            if(user!=null) {
                Log.d(TAG, "${user.email} SIGNED IN")
                toastMessage("${user.email} SIGNED IN ")
            }else {
                Log.d(TAG, "SIGNED OUT")
                toastMessage("SIGNED OUT")
            }
        }


        binding.signinButton.setOnClickListener{
            val email=binding.emialEdittext.text.toString()
            val pass=binding.passwordEdittext.text.toString()
            if(!email.equals("")&&!pass.equals("")){
                mAuth.signInWithEmailAndPassword(email,pass)
                toastMessage("Welcome!")
            }else
            {
                toastMessage("Fields cannot be empty")
            }
        }
         binding.signoutButton.setOnClickListener {
             mAuth.signOut()
         }
        binding.infoButton.setOnClickListener {
            val intent= Intent(this@MainActivity,EntryPage::class.java)
               startActivity(intent)
         }
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
        Toast.makeText(this,Str,Toast.LENGTH_SHORT).show()
    }
}