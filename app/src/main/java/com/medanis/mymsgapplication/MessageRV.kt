package com.medanis.mymsgapplication

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView

@Suppress("DEPRECATION")
class MessageRV(private val mContext: Context, var mData: MutableList<Messages>) : RecyclerView.Adapter<MessageRV.MessageRV>() {

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageRV {
        val view: View
        val mInflater = LayoutInflater.from(mContext)
        view = mInflater.inflate(R.layout.message_bubble, parent, false)
        return MessageRV(view)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: MessageRV, position: Int) {
        holder.setIsRecyclable(false)

        holder.msgBody.text = mData[position].message

    }
    inner class MessageRV(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal  var msgBody = itemView.findViewById<TextView>(R.id.messageBody)
        internal  var cardView = itemView.findViewById<CardView>(R.id.cardView)
    }

}

