package com.tigerteco.vidcall.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codewithkael.webrtcprojectforrecord.SocketManger
import com.codewithkael.webrtcprojectforrecord.WebRTCManager
import com.tigerteco.vidcall.R
import com.tigerteco.vidcall.databinding.FragmentInCommingBinding
import com.tigerteco.vidcall.model.socketModel.MessageModel

var voiceCallType = "Voice Call"
var videoCallType = "Video Call"

class InComingFragment(val webRTCManager: WebRTCManager,val socketManger: SocketManger,val message: MessageModel,val callType: String,val target: String) : Fragment() {

    lateinit var binding : FragmentInCommingBinding

    companion object{

        val TAG : String? = InComingFragment::class.simpleName




    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = FragmentInCommingBinding.inflate(inflater , container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var callType = arguments?.getString("callType")
        binding.txtIncomingVoiceCallFragment.text = callType
            binding.txtUsernameImcomingCallFragment.text =arguments?.getString("target")
        binding.declineIncomingCallFragment.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .remove(this)
                .commit()
        }

        binding.acceptIncomingFragment.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_main , VideoCallFragment(webRTCManager = webRTCManager,socketManger, message = message),VideoCallFragment.TAG)
                .commit()

        }


    }

}