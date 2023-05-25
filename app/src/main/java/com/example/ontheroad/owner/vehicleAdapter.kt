package com.example.ontheroad.owner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ontheroad.R

class vehicleAapter(private val userList:ArrayList<vehicle_data>): RecyclerView.Adapter<vehicleAapter.MyviewHolder>() {
    private lateinit var mitem: onItemClickListener

    interface onItemClickListener {
        fun itemclick(position: Int, text: vehicle_data)
    }

    fun onItemClickLisnear(listener: onItemClickListener) {
        mitem = listener

    }


    class MyviewHolder(
        itemView: View,
        listener: onItemClickListener,
        val userList: ArrayList<vehicle_data>
    ) :
        RecyclerView.ViewHolder(itemView) {
        val vehicle_no: TextView = itemView.findViewById(R.id.vehicalNO)
        val vehicle_type: TextView = itemView.findViewById(R.id.vehicalType)

        init {
            itemView.setOnClickListener {
                val (position, user) = it.tag as Pair<Int, vehicle_data>
                listener.itemclick(position, user)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.add_vehicle_recycler, parent, false)
        return MyviewHolder(itemView, mitem, userList)
    }

    override fun onBindViewHolder(holder: MyviewHolder, position: Int) {
        holder.vehicle_no.text = userList[position].Vehicle_No
        holder.vehicle_type.text = userList[position].Vehicle_Type
        holder.itemView.tag = Pair(position, userList[position])


    }

    override fun getItemCount(): Int {
        return userList.size
    }
}