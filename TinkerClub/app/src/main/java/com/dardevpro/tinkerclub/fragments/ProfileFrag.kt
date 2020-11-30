package com.dardevpro.tinkerclub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.dardevpro.tinkerclub.R
import com.dardevpro.tinkerclub.activiter.TinkerCallback
import com.dardevpro.tinkerclub.util.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFrag : Fragment() {

    private lateinit var  userId:String
    private lateinit var userDatabase:DatabaseReference
    private var callback:TinkerCallback? =null

    fun setCallback(callback:TinkerCallback){
        this.callback=callback
        userId=callback.onGetUserId()
        userDatabase=callback.getUserDatabase().child(userId)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressLayout.setOnTouchListener{view,event->true}

        populateInfo()

        photoIV.setOnClickListener { callback?.startActivityForPhoto() }

        applyButton.setOnClickListener{onApply()}
        signoutButton.setOnClickListener{callback?.onSignout()}
    }

    fun populateInfo(){
        progressLayout.visibility=View.VISIBLE
        userDatabase.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                progressLayout.visibility=View.GONE
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(isAdded){
                    val user=p0.getValue(User::class.java)
                    nameET.setText(user?.nom,TextView.BufferType.EDITABLE)
                    emailET.setText(user?.email,TextView.BufferType.EDITABLE)
                    ageET.setText(user?.age,TextView.BufferType.EDITABLE)
                    if(user?.genre== GENDER_MALE){
                        radioMan.isChecked=true
                    }
                    if(user?.genre== GENDER_FEMALE){
                        radioWoman.isChecked=true
                    }

                    if(user?.genreFavori== GENDER_MALE){
                        radioMan2.isChecked=true
                    }
                    if(user?.genreFavori== GENDER_FEMALE){
                        radioWoman2.isChecked=true
                    }
                    if(!user?.imageUrl.isNullOrEmpty()){
                        populateImage(user?.imageUrl!!)
                    }
                    progressLayout.visibility=View.GONE
                }
            }
        })
    }

    fun onApply(){
        if(nameET.text.toString().isNullOrEmpty()||
                emailET.text.toString().isNullOrEmpty()||
                    radioGroup1.checkedRadioButtonId==-1||
                        radioGroup2.checkedRadioButtonId==-1){
            Toast.makeText(context,"Please complete your profile",Toast.LENGTH_SHORT).show()
        }else{
            val name=nameET.text.toString()
            val age=ageET.text.toString()
            val email=emailET.text.toString()
            val gender=
                    if(radioMan.isChecked) GENDER_MALE
                    else GENDER_FEMALE
            val pref=
                    if(radioMan2.isChecked) GENDER_MALE
                    else GENDER_FEMALE
            userDatabase.child(DATA_NOM).setValue(name)
            userDatabase.child(DATA_AGE).setValue(age)
            userDatabase.child(DATA_EMAIL).setValue(email)
            userDatabase.child(DATA_GENRE).setValue(gender)
            userDatabase.child(DATA_GENRE_PREF).setValue(pref)

            callback?.profileComplete()
            Toast.makeText(context,"Profil Completer",Toast.LENGTH_SHORT).show()
        }
    }

    fun updateImageUri(uri:String){
        userDatabase.child(DATA_IMAGE_URL).setValue(uri)
        populateImage(uri)
    }

    fun populateImage(uri:String){
        Glide.with(this)
                .load(uri)
                .into(photoIV)
    }
}