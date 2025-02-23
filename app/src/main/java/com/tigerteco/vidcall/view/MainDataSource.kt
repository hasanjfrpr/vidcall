package com.tigerteco.vidcall.view

import com.tigerteco.vidcall.localData.LocalSource


interface MainDataSource{
    fun getUsername():String
}

class MainLocalDataSource(val localSource : LocalSource) : MainDataSource  {


    override fun getUsername(): String  = localSource.getUserName()
}