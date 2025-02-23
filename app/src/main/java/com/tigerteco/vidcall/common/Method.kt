package com.tigerteco.vidcall.common

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tigerteco.vidcall.model.socketModel.MessageModel
import org.json.JSONObject


fun toast(context:Context , message:String){
    Toast.makeText(context , message , Toast.LENGTH_SHORT).show()
}

val gson = Gson()

@SuppressLint("SuspiciousIndentation")
fun createJsonObject(name:String, target:String?, type:String, data:Any? ):JSONObject{


  val json =   gson.toJson(MessageModel(
        name =name,
        target = target,
        type = type,
        data = data
    ))

    return  JSONObject(json)
}

@SuppressLint("SuspiciousIndentation")
fun createObjectFromJson(args : Array<Any>) : MessageModel{
    var messageModel : MessageModel? = MessageModel("");
    if (args.isNotEmpty()) {
        val message = args[0] as JSONObject
        try {
             messageModel = gson.fromJson(message.toString(), MessageModel::class.java)
                return messageModel
        } catch (e: Exception) {
            messageModel = null
            Log.e("jsonError", "Error parsing message: $e")
        }
    }
    return messageModel!!;
}