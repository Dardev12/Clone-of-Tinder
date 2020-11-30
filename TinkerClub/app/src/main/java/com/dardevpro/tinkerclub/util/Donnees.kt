package com.dardevpro.tinkerclub.util

data class User(
    val uid:String ="",
    val nom:String ="",
    val age:String ="",
    val email:String ="",
    val genre:String="",
    val genreFavori:String ="",
    val imageUrl:String =""

)

data class Chat(
    val userId: String? = "",
    val chatId: String? = "",
    val otherUserId: String? = "",
    val name: String? = "",
    val imageUrl: String? = ""
)

data class Message(
    val sentBy: String? = null,
    val message: String? = null,
    val time: String? = null
)