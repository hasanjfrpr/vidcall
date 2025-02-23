package com.tigerteco.vidcall.model.socketModel

data class MessageModel(
    val name : String ,
    val target : String? = null,
    val type : String? = null,
    val data : Any? = null
)

