package com.tigerteco.vidcall.view



interface  MainRepo{
    fun getUsername():String
}


class MainRepoImpl(val mainDataSource: MainDataSource) : MainRepo {


    override fun getUsername(): String = mainDataSource.getUsername()
}