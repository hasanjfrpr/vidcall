package com.codewithkael.webrtcprojectforrecord

import android.util.Log
import com.google.gson.Gson
import com.tigerteco.vidcall.model.socketModel.MessageModel
import io.socket.client.IO
import io.socket.emitter.Emitter
import org.json.JSONObject

class SocketManger(private val messageInterface: NewMessageInterface) {
    private var socket:  io.socket.client.Socket? = null
    private var userName: String? = null
    private val TAG = "SocketRepository"
    private val gson = Gson()

    fun initSocket(username: String) {
        userName = username
        try {
            // Connect to your server. Adjust the URL for emulator or real device testing.
            // Emulator: ws://10.0.2.2:3000
            // Real device: Replace with your system's IP or deployed server URL.
            socket = IO.socket("http://192.168.2.15:3000")
        } catch (e: Exception) {
            Log.e(TAG, "Socket initialization error: $e")
        }

        // Listen for connection events
        socket?.on(io.socket.client.Socket.EVENT_CONNECT) {
            Log.d(TAG, "Connected to socket")
            sendMessageToSocket(
                MessageModel(
                    type = "store_user",
                    name = username,
                    target = null,
                    data = null
                )
            )
        }

        // Listen for incoming messages
        socket?.on("message", Emitter.Listener { args ->
            if (args.isNotEmpty()) {
                val message = args[0] as JSONObject
                try {
                    val messageModel = gson.fromJson(message.toString(), MessageModel::class.java)
                    messageInterface.onNewMessage(messageModel)
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing message: $e")
                }
            }
        })

        // Handle disconnection
        socket?.on(io.socket.client.Socket.EVENT_DISCONNECT) {
            Log.d(TAG, "Socket disconnected")
        }

        // Handle errors
        socket?.on(io.socket.client.Socket.EVENT_CONNECT_ERROR) { args ->
            Log.e(TAG, "Connection error: ${args.firstOrNull()}")
        }

        socket?.connect()
    }

    fun sendMessageToSocket(message: MessageModel) {
        try {
            val jsonMessage = gson.toJson(message)
            Log.d(TAG, "Sending message: $jsonMessage")
            socket?.emit("message", JSONObject(jsonMessage))
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: $e")
        }
    }

    fun disconnectSocket() {
        socket?.disconnect()
        socket?.off() // Remove all listeners
    }
}

interface NewMessageInterface {
    fun onNewMessage(message: MessageModel)
}