package com.tigerteco.vidcall.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.codewithkael.webrtcprojectforrecord.SocketManger
import com.codewithkael.webrtcprojectforrecord.WebRTCManager
import com.tigerteco.vidcall.R
import com.tigerteco.vidcall.databinding.FragmentVideoCallBinding
import com.tigerteco.vidcall.model.socketModel.MessageModel
import org.webrtc.MediaStream
import org.webrtc.SessionDescription

class VideoCallFragment(val webRTCManager: WebRTCManager,val socketManger: SocketManger,val message: MessageModel) : Fragment(), AddStream {

    lateinit var binding : FragmentVideoCallBinding

    var dX = 0f
    var dY = 0f

    companion object{

        val TAG : String? = VideoCallFragment::class.simpleName

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVideoCallBinding.inflate(inflater,container,false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        ContactFragment.addStream = this


        draggingLocalView()

        init()



    }

    fun init(){

        binding.apply {
            webRTCManager.initializeSurfaceView(binding.localViewCallFragment)
            webRTCManager.initializeSurfaceView(binding.remoteViewCallFragment)
            webRTCManager.startLocalVideo(binding.localViewCallFragment)
        }
        val session = SessionDescription(
            SessionDescription.Type.OFFER,
            message.data.toString()
        )
        webRTCManager.onRemoteSessionReceived(session)
        webRTCManager.answer(message.name)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun draggingLocalView(){

        binding.localViewCallFragment.setOnTouchListener { view, event ->

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = event.rawX - view.x
                    dY = event.rawY - view.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val parent = view.parent as View
                    val parentWidth = parent.width - view.width
                    val parentHeight = parent.height - view.height

                    // Constrain movement within the parent bounds
                    val newX = (event.rawX - dX).coerceIn(0f, parentWidth.toFloat())
                    val newY = (event.rawY - dY).coerceIn(0f, parentHeight.toFloat())

                    view.x = newX
                    view.y = newY

                   
                }
            }
            true
        }
    }

    override fun onAddStream(mediaStream: MediaStream) {

        mediaStream.videoTracks!![0]!!.addSink(binding.remoteViewCallFragment)


            }


}

