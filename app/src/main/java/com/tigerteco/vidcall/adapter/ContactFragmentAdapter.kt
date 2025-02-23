package com.tigerteco.vidcall.adapter

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.tigerteco.vidcall.R
import com.tigerteco.vidcall.model.ContactModel

class ContactFragmentAdapter(val contactList : List<ContactModel>) : RecyclerView.Adapter<ContactFragmentAdapter.ContactViewHolder>(){


        lateinit var recyclerEvent : RecyclerEvent


    inner class ContactViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_contact_fragment , parent, false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int  = contactList.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        var toUser = contactList[position].userName

        holder.itemView.findViewById<ImageView>(R.id.ic_video_call_contact_fragment).setOnClickListener{recyclerEvent.onVideoCallTap(toUser)}
        holder.itemView.findViewById<ImageView>(R.id.ic_voice_call_contact_fragment).setOnClickListener{recyclerEvent.onVoiceCallTap(toUser)}
        holder.itemView.findViewById<TextView>(R.id.txt_username_contact_fragment).text =toUser
    }


    interface RecyclerEvent{

        fun onVoiceCallTap(toUser : String)
        fun onVideoCallTap(toUser : String)
    }



}
