package com.dardevpro.tinkerclub.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dardevpro.tinkerclub.R
import com.dardevpro.tinkerclub.util.User
import com.dardevpro.tinkerclub.activiter.UserInfoActivity

class CardsAdapter(context: Context?, resourceId: Int, users: List<User>): ArrayAdapter<User>(
    context!!, resourceId, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var user = getItem(position)
        var finalView = convertView ?: LayoutInflater.from(context).inflate(R.layout.citem, parent, false)

        var name = finalView.findViewById<TextView>(R.id.nameTV)
        var image = finalView.findViewById<ImageView>(R.id.photoIV)
        var userInfo = finalView.findViewById<LinearLayout>(R.id.userInfoLayout)
        name.text = "${user?.nom}, ${user?.age}"

            Glide.with(context)
                .load(user?.imageUrl)
                .into(image)


        userInfo.setOnClickListener {

                finalView.context.startActivity(UserInfoActivity.newIntent(finalView.context, user?.uid))

        }

        return finalView
    }
}