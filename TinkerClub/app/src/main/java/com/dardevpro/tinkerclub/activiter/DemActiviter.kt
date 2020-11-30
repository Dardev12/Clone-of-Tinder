package com.dardevpro.tinkerclub.activiter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dardevpro.tinkerclub.R

class DemActiviter : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dem_activiter)
    }

    fun onLogin(v: View){
        startActivity(loginActiviter.newIntent(this))
    }

    fun onSignUp(v:View){
        startActivity(SignupActiviter.newIntent(this))
    }
    companion object{
        fun newIntent(context: Context?)= Intent(context, DemActiviter::class.java)
    }
}