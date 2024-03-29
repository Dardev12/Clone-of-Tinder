package com.dardevpro.tinkerclub.activiter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TableLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dardevpro.tinkerclub.R
import com.dardevpro.tinkerclub.fragments.MatchesFrag
import com.dardevpro.tinkerclub.fragments.ProfileFrag
import com.dardevpro.tinkerclub.fragments.SwipeFrag
import com.dardevpro.tinkerclub.util.DATA_USERS
import com.dardevpro.tinkerclub.util.DATA_CHATS
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.io.IOException

const val  REQUEST_CODE_PHOTO=1234

class TinkerActivity : AppCompatActivity(),TinkerCallback {

    private val firebaseAuth=FirebaseAuth.getInstance()
    private val userId=firebaseAuth.currentUser?.uid
    private lateinit var userDatabase:DatabaseReference
    private lateinit var chatDatabase: DatabaseReference

    private var profileFrag:ProfileFrag?=null
    private var swipeFrag:SwipeFrag?=null
    private var matchesFrag:MatchesFrag?=null

    private var profileTab: TabLayout.Tab? =null
    private var swipeTab:TabLayout.Tab? =null
    private var matchesTab:TabLayout.Tab? =null

    private var resultImageUrl: Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(userId.isNullOrEmpty()){
            onSignout()
        }

        userDatabase=FirebaseDatabase.getInstance().reference.child(DATA_USERS)
        chatDatabase=FirebaseDatabase.getInstance().reference.child(DATA_CHATS)

        profileTab=navigationTabs.newTab()
        swipeTab=navigationTabs.newTab()
        matchesTab=navigationTabs.newTab()

        profileTab?.icon=ContextCompat.getDrawable(this,R.drawable.tab_profile)
        swipeTab?.icon=ContextCompat.getDrawable(this,R.drawable.tab_swipe)
        matchesTab?.icon=ContextCompat.getDrawable(this,R.drawable.tab_matches)

        navigationTabs.addTab(profileTab!!)
        navigationTabs.addTab(swipeTab!!)
        navigationTabs.addTab(matchesTab!!)

        navigationTabs.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
                onTabSelected(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab){
                    profileTab->{
                        if (profileFrag==null){
                            profileFrag= ProfileFrag()
                            profileFrag!!.setCallback(this@TinkerActivity)
                        }
                        replaceFragment(profileFrag!!)
                    }
                    swipeTab->{
                        if(swipeFrag==null){
                            swipeFrag=SwipeFrag()
                            swipeFrag!!.setCallback(this@TinkerActivity)
                        }
                        replaceFragment(swipeFrag!!)
                    }
                    matchesTab->{
                        if (matchesFrag==null){
                            matchesFrag= MatchesFrag()
                            matchesFrag!!.setCallback(this@TinkerActivity)
                        }
                        replaceFragment(matchesFrag!!)
                    }
                }
            }

        })
        profileTab?.select()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.FragContains,fragment)
            .commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==Activity.RESULT_OK&&requestCode== REQUEST_CODE_PHOTO){
            resultImageUrl=data?.data
            storeImage()

        }
    }
    fun storeImage(){
        if(resultImageUrl!=null&& userId!=null){
            val filePath=FirebaseStorage.getInstance().reference.child("profileImage").child(userId)
            var bitmap:Bitmap? =null
            try{
                bitmap=MediaStore.Images.Media.getBitmap(application.contentResolver,resultImageUrl)
            }catch(e:IOException){
                e.printStackTrace()
            }

            val baos=ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG,20,baos)
            val data=baos.toByteArray()

            val uploadTask=filePath.putBytes(data)
            uploadTask.addOnFailureListener{e->e.printStackTrace()}
            uploadTask.addOnSuccessListener { tasksnapshot->
                filePath.downloadUrl
                        .addOnSuccessListener { uri->
                            profileFrag?.updateImageUri(uri.toString())
                        }
                        .addOnFailureListener{e->e.printStackTrace()}
            }
        }
    }

    override fun onSignout() {
        firebaseAuth.signOut()
        startActivity(DemActiviter.newIntent(this))
        finish()
    }

    override fun onGetUserId(): String=userId!!

    override fun profileComplete() {
        swipeTab?.select()
    }

    override fun startActivityForPhoto() {
        val intent=Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent, REQUEST_CODE_PHOTO)
    }
    override fun getUserDatabase(): DatabaseReference=userDatabase
    override fun getChatDatabase(): DatabaseReference = chatDatabase
    companion object{
        fun newIntent(context: Context?)= Intent(context, TinkerActivity::class.java)
    }
}

