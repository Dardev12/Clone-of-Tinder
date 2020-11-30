package com.dardevpro.tinkerclub.activiter

import com.google.firebase.database.DatabaseReference

interface TinkerCallback {
    fun onSignout()
    fun onGetUserId():String
    fun getUserDatabase():DatabaseReference
    fun getChatDatabase(): DatabaseReference
    fun profileComplete()
    fun startActivityForPhoto()

}