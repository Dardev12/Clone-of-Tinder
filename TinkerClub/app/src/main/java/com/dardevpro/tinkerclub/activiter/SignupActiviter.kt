package com.dardevpro.tinkerclub.activiter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.dardevpro.tinkerclub.R
import com.dardevpro.tinkerclub.util.DATA_USERS
import com.dardevpro.tinkerclub.util.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup_activiter.*

class SignupActiviter : AppCompatActivity() {
    private val fifiAuthDatabase=FirebaseDatabase.getInstance().reference
    private val fifiAuth= FirebaseAuth.getInstance()
    private val fifiAuthListener=FirebaseAuth.AuthStateListener {
        val user = fifiAuth.currentUser
        if (user != null){
            startActivity(TinkerActivity.newIntent(this))
            finish()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_activiter)
    }

    override fun onStart() {
        super.onStart()
        fifiAuth.addAuthStateListener(fifiAuthListener)
    }

    override fun onStop(){
        super.onStop()
        fifiAuth.removeAuthStateListener(fifiAuthListener)
    }
    fun onSignUp(v:View){
        if(!emailET.text.toString().isNullOrEmpty() && !passwordET.text.toString().isNullOrEmpty()){
            fifiAuth.createUserWithEmailAndPassword(emailET.text.toString(),passwordET.text.toString())
                    .addOnCompleteListener{task->
                        if (!task.isSuccessful){
                            Toast.makeText(this,"Erreur Signup ${task.exception?.localizedMessage}",Toast.LENGTH_SHORT).show()
                        }else{
                            val email=emailET.text.toString()
                            val Iduser=fifiAuth.currentUser?.uid?:""
                            val user=User(Iduser,"","",email,"","","")
                            fifiAuthDatabase.child(DATA_USERS).child(Iduser).setValue(user)
                        }
                    }
        }
    }
    companion object{
        fun newIntent(context: Context?)= Intent(context,SignupActiviter::class.java)
    }
}