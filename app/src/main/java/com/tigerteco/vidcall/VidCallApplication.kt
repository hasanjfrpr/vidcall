package com.tigerteco.vidcall

import android.app.Application
import com.tigerteco.vidcall.localData.SharedManager
import com.tigerteco.vidcall.service.koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class VidCallApplication : Application() {


    override fun onCreate() {
        super.onCreate()


        startKoin{
            androidContext(this@VidCallApplication)
            modules(appModule)
        }



    }




}