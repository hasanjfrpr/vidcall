package com.tigerteco.vidcall.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tigerteco.vidcall.databinding.ActivityMainBinding
import com.tigerteco.vidcall.view.fragments.AuthFragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.lifecycle.ViewModelProvider
import com.codewithkael.webrtcprojectforrecord.NewMessageInterface
import com.codewithkael.webrtcprojectforrecord.SocketManger
import com.codewithkael.webrtcprojectforrecord.WebRTCManager
import com.tigerteco.vidcall.R
import com.tigerteco.vidcall.common.PeerObserver
import com.tigerteco.vidcall.localData.LocalSource
import com.tigerteco.vidcall.localData.SharedManager
import com.tigerteco.vidcall.model.socketModel.MessageModel
import com.tigerteco.vidcall.view.fragments.VideoCallFragment
import com.tigerteco.vidcall.view.fragments.ContactFragment
import com.tigerteco.vidcall.view.fragments.InComingFragment
import com.tigerteco.vidcall.viewModel.MainViewModel
import com.tigerteco.vidcall.viewModel.MainViewModelFactory
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import java.net.Socket


const val REQUEST_CAMERA_PERMISSION: Int = 100

const val REQUEST_AUDIO_PERMISSION: Int = 101


class MainActivity : AppCompatActivity(){

    lateinit var socketManger: SocketManger
    lateinit var webRTCManager: WebRTCManager
    lateinit var sharedManager: SharedManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()

        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        init()





        if(sharedManager.getUserName().isEmpty()){
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_main , AuthFragment(sharedManager) , AuthFragment.TAG)
                .commit()

        }else{

            supportFragmentManager.beginTransaction()
                .add(R.id.frame_main , ContactFragment(sharedManager.getUserName()) , ContactFragment.TAG)
                .commit()

        }







    }

    fun init(){
        sharedManager = SharedManager(applicationContext.getSharedPreferences("userDB" ,  MODE_PRIVATE))

    }




}