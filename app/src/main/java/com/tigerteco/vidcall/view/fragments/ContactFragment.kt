package com.tigerteco.vidcall.view.fragments

import android.os.Bundle
import android.provider.MediaStore.Video
import android.telecom.InCallService.VideoCall
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codewithkael.webrtcprojectforrecord.NewMessageInterface
import com.codewithkael.webrtcprojectforrecord.SocketManger
import com.codewithkael.webrtcprojectforrecord.WebRTCManager
import com.tigerteco.vidcall.R
import com.tigerteco.vidcall.adapter.ContactFragmentAdapter
import com.tigerteco.vidcall.common.PeerObserver
import com.tigerteco.vidcall.common.RTCAudioManager
import com.tigerteco.vidcall.common.gson
import com.tigerteco.vidcall.common.toast
import com.tigerteco.vidcall.databinding.FragmentContactBinding
import com.tigerteco.vidcall.model.ContactModel
import com.tigerteco.vidcall.model.IceCandidateModel
import com.tigerteco.vidcall.model.socketModel.MessageModel
import com.tigerteco.vidcall.viewModel.MainViewModel
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.SessionDescription


class ContactFragment(val userName : String) : Fragment() , ContactFragmentAdapter.RecyclerEvent,NewMessageInterface {
    lateinit var binding : FragmentContactBinding
    lateinit var socketManger: SocketManger
    lateinit var webRTCManager: WebRTCManager
    var target : String = ""
    private var isMute = false
    private var isCameraPause = false
    private var isSpeakerMode = true
    private val rtcAudioManager by lazy { RTCAudioManager.create(requireContext()) }

    companion object{
        val TAG: String? = ContactFragment::class.simpleName;
        lateinit var addStream : AddStream
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentContactBinding.inflate(inflater , container   , false )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        initProperties()
        getContact()

        onVideoCallButtonClickListener()



    }

    fun onVideoCallButtonClickListener(){
        binding.apply {

            switchCameraButton.setOnClickListener {
                webRTCManager.switchCamera()
            }

            micButton.setOnClickListener {
                if (isMute){
                    isMute = false
                    micButton.setImageResource(R.drawable.ic_baseline_mic_off_24)
                }else{
                    isMute = true
                    micButton.setImageResource(R.drawable.ic_baseline_mic_24)
                }
                webRTCManager.toggleAudio(isMute)
            }

            videoButton.setOnClickListener {
                if (isCameraPause){
                    isCameraPause = false
                    videoButton.setImageResource(R.drawable.ic_baseline_videocam_off_24)
                }else{
                    isCameraPause = true
                    videoButton.setImageResource(R.drawable.ic_baseline_videocam_24)
                }
                webRTCManager.toggleCamera(isCameraPause)
            }

            audioOutputButton.setOnClickListener {
                if (isSpeakerMode){
                    isSpeakerMode = false
                    audioOutputButton.setImageResource(R.drawable.ic_baseline_hearing_24)
                    rtcAudioManager.setDefaultAudioDevice(RTCAudioManager.AudioDevice.EARPIECE)
                }else{
                    isSpeakerMode = true
                    audioOutputButton.setImageResource(R.drawable.ic_baseline_speaker_up_24)
                    rtcAudioManager.setDefaultAudioDevice(RTCAudioManager.AudioDevice.SPEAKER_PHONE)

                }

            }
            endCallButton.setOnClickListener {
                binding.layoutVideoCall.visibility = View.GONE
                binding.layoutContact.visibility = View.VISIBLE
                binding.layoutIncomingCall.visibility = View.GONE
                webRTCManager.endCall()
            }
        }
    }

fun initProperties(){
    socketManger = SocketManger(this)
    socketManger.initSocket(userName)
    webRTCManager = WebRTCManager(requireActivity().application,userName,socketManger,object : PeerObserver(){
        override fun onIceCandidate(p0: IceCandidate?) {
            webRTCManager.addIceCandidate(p0)
            val candidate = hashMapOf(
                "sdpMid" to p0?.sdpMid,
                "sdpMLineIndex" to p0?.sdpMLineIndex,
                "sdpCandidate" to p0?.sdp
            )


            socketManger.sendMessageToSocket(
                MessageModel(type = "ice_candidate", name = userName, target = target, data = candidate)
            )
        }

        override fun onAddStream(p0: MediaStream?) {

            p0!!.videoTracks!![0]!!.addSink(binding.remoteViewCallFragment)
        }
    })
}

    fun getContact(){
        socketManger.sendMessageToSocket(
            MessageModel(
            name = userName,
            target = target,
            type = "contacts",
            data = null
        )
        )
    }

    fun initRecycler(sampleList:MutableList<ContactModel>){

        sampleList.remove(ContactModel(userName = userName));

        if(sampleList.isEmpty()){
            binding.emptyContactFragment.visibility = View.VISIBLE
            binding.rcContactFragment.visibility = View.GONE
        }else{
            binding.emptyContactFragment.visibility = View.GONE
            binding.rcContactFragment.visibility = View.VISIBLE
            var adapter = ContactFragmentAdapter(sampleList)
            binding.rcContactFragment.adapter = adapter
            binding.rcContactFragment.layoutManager = LinearLayoutManager(context , RecyclerView.VERTICAL , false)

            adapter.recyclerEvent = this;

        }

    }



    override fun onVoiceCallTap(toUser: String) {

    }

    override fun onVideoCallTap(toUser:String) {

        socketManger.sendMessageToSocket(
            MessageModel(
            name = userName,
            type = "start_call",
            target = toUser,
            data = null
        ))
        target = toUser


    }

    override fun onNewMessage(message: MessageModel) {
       when(message.type){



           "contacts"->{
               var sampleList:MutableList<ContactModel> = ArrayList()
               var list  = message.data as List<String>
               for (item in list) {
                   sampleList.add(ContactModel(userName = item.toString()))
               }
               var sampleSet = sampleList.toSet();

               requireActivity().runOnUiThread{

                   initRecycler(sampleSet.toMutableList())

               }
               Log.i("Contacts" , message.data.toString())
           }
           "call_response"->{
               if (message.data == "user is not online"){
                   //user is not reachable
                   requireActivity().runOnUiThread {
                       toast(requireContext(),"user is not reachable")

                   }

               }else{
                   //we are ready for call, we started a call
                   requireActivity().runOnUiThread {


                       binding.apply {
                          webRTCManager.initializeSurfaceView(localViewCallFragment)
                           webRTCManager.initializeSurfaceView(remoteViewCallFragment)
                           webRTCManager.startLocalVideo(localViewCallFragment)
                           webRTCManager.call(target)
                       }



                   }

               }
           }
           "answer_received"->{

               binding.txtUsernameImcomingCallFragment.text = target;

               val session = SessionDescription(
                   SessionDescription.Type.ANSWER,
                   message.data.toString()
               )
              webRTCManager.onRemoteSessionReceived(session)

                   requireActivity().runOnUiThread {
                       binding.layoutVideoCall.visibility = View.VISIBLE
                       binding.layoutContact.visibility = View.GONE
                   }

           }
           "offer_received"->{
              requireActivity().runOnUiThread {
                binding.layoutIncomingCall.visibility = View.VISIBLE;
                  binding.layoutVideoCall.visibility = View.GONE
                  binding.layoutContact.visibility = View.GONE
                  binding.acceptIncomingFragment.setOnClickListener{
                      binding.layoutIncomingCall.visibility = View.GONE
                     binding.layoutVideoCall.visibility = View.VISIBLE
                      binding.layoutIncomingCall.visibility = View.GONE

                      binding.apply {
                          webRTCManager.initializeSurfaceView(localViewCallFragment)
                          webRTCManager.initializeSurfaceView(remoteViewCallFragment)
                          webRTCManager.startLocalVideo(localViewCallFragment)
                      }
                      val session = SessionDescription(
                          SessionDescription.Type.OFFER,
                          message.data.toString()
                      )
                      webRTCManager.onRemoteSessionReceived(session)
                      webRTCManager.answer(message.name)
                      target = message.name
//                      binding.remoteViewLoading.visibility = View.GONE
                  }
                  binding.declineIncomingCallFragment.setOnClickListener{
                            binding.layoutIncomingCall.visibility = View.GONE
                      binding.layoutContact.visibility = View.VISIBLE
                  }
//                  binding.acceptButton.setOnClickListener {
//                      setIncomingCallLayoutGone()
//                      setCallLayoutVisible()
//                      setWhoToCallLayoutGone()
//
//                      binding.apply {
//                          rtcClient?.initializeSurfaceView(localView)
//                          rtcClient?.initializeSurfaceView(remoteView)
//                          rtcClient?.startLocalVideo(localView)
//                      }
//                      val session = SessionDescription(
//                          SessionDescription.Type.OFFER,
//                          message.data.toString()
//                      )
//                      rtcClient?.onRemoteSessionReceived(session)
//                      rtcClient?.answer(message.name!!)
//                      target = message.name!!
//                      binding.remoteViewLoading.visibility = View.GONE
//
//                  }
//                  binding.rejectButton.setOnClickListener {
//                      setIncomingCallLayoutGone()
//                  }
              }
           }

           "ice_candidate"->{
               try {
                   val receivingCandidate = gson.fromJson(gson.toJson(message.data),
                       IceCandidateModel::class.java)
                   webRTCManager.addIceCandidate(IceCandidate(receivingCandidate.sdpMid,
                       Math.toIntExact(receivingCandidate.sdpMLineIndex.toLong()),receivingCandidate.sdpCandidate))
               }catch (e:Exception){
                   e.printStackTrace()
               }

           }

       }
    }

}

interface  AddStream{
    fun onAddStream(mediaStream: MediaStream)
}