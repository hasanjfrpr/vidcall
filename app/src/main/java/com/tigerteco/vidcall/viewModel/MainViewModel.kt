package com.tigerteco.vidcall.viewModel

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.codewithkael.webrtcprojectforrecord.SocketManger
import com.codewithkael.webrtcprojectforrecord.WebRTCManager
import com.tigerteco.vidcall.common.PeerObserver
import com.tigerteco.vidcall.view.MainRepo
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.RtpReceiver

class MainViewModel(val application: Application,val socketManager:SocketManger, val webRTCManager: WebRTCManager, val repo : MainRepo) : ViewModel() , DefaultLifecycleObserver{

lateinit var peerObserver: PeerObserver


init {


}

interface PeerCoObserver{
    fun onIceCandidate(iceCandidate: IceCandidate)
    fun onAddStream(mediaStream: MediaStream)
}



    override fun onDestroy(owner: LifecycleOwner) {

      socketManager.disconnectSocket()

        super.onDestroy(owner)

    }

}