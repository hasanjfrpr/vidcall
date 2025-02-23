package com.tigerteco.vidcall.localData

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class SharedManager(private val sharedPreferences: SharedPreferences) : LocalSource {

    private  val editor : SharedPreferences.Editor by lazy {
        sharedPreferences.edit()
    }





    override fun saveUser(username: String) {
        editor.putString("userName" , username).apply()
    }

    override fun getUserName(): String = sharedPreferences.getString("userName" , "") ?: ""


}








interface LocalSource{

    fun saveUser(username:String)
    fun getUserName() : String

}