package com.tigerteco.vidcall.view.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.replace
import com.tigerteco.vidcall.R
import com.tigerteco.vidcall.common.toast
import com.tigerteco.vidcall.databinding.FragmentAuthBinding
import com.tigerteco.vidcall.localData.LocalSource
import com.tigerteco.vidcall.localData.SharedManager
import com.tigerteco.vidcall.view.REQUEST_AUDIO_PERMISSION
import com.tigerteco.vidcall.view.REQUEST_CAMERA_PERMISSION
import com.tigerteco.vidcall.viewModel.MainViewModel
import org.koin.android.ext.android.inject

class AuthFragment(val sharedManager: SharedManager) : Fragment() {

    lateinit var binding: FragmentAuthBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(sharedManager.getUserName().isNotEmpty()){
            binding.cardviewAuthFragment.visibility = View.GONE
            binding.progressBarAuthFragment.visibility = View.VISIBLE
            requestPermission()

        }else{
            binding.cardviewAuthFragment.visibility = View.VISIBLE
            binding.progressBarAuthFragment.visibility = View.GONE

        }


        binding.mbtnRegister.setOnClickListener{


            requestPermission()

        }



    }


    private fun requestPermission(){
        if(context?.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            activity?.requestPermissions(arrayOf(Manifest.permission.CAMERA) , REQUEST_CAMERA_PERMISSION)
        }
        else if(context?.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            activity?.requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO) , REQUEST_AUDIO_PERMISSION)
        }else{

            if(sharedManager.getUserName().isEmpty()){

                if(binding.textFieldRegister.text!!.isEmpty()){
                    toast(requireActivity() , "please enter your username")
                }else{
                    sharedManager.saveUser(binding.textFieldRegister.text.toString())
                }

            }
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_main , ContactFragment(binding.textFieldRegister.text.toString()) , ContactFragment.TAG)
                    .commit()






        }


    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CAMERA_PERMISSION -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    requestPermission()
                }
            }
            REQUEST_AUDIO_PERMISSION -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    requestPermission()
                }
            }else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    companion object {
        val TAG: String? = AuthFragment::class.simpleName
    }


}