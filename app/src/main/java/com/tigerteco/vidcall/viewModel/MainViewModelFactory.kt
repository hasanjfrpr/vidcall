package com.tigerteco.vidcall.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codewithkael.webrtcprojectforrecord.SocketManger
import com.codewithkael.webrtcprojectforrecord.WebRTCManager
import com.tigerteco.vidcall.view.MainRepo

class MainViewModelFactory(val application: Application,val socketManger: SocketManger,val webRTCManager: WebRTCManager,val repo: MainRepo) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(application , socketManger , webRTCManager , repo) as T
    }
}