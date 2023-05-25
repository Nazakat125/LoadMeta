package com.example.ontheroad.driver

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ontheroad.R

class CvAdapter(private val driverList:ArrayList<cvData>): RecyclerView.Adapter<CvAdapter.hold>() {
    private lateinit var mitem:itemClick
    interface itemClick{
        fun clickItem(position: Int, user: cvData)
    }
    fun itemClick(listener:itemClick){
        mitem = listener

    }
    class hold(itemView: View, listener:itemClick, val userList:ArrayList<cvData>): RecyclerView.ViewHolder(itemView){

        val nam: TextView = itemView.findViewById(R.id.driver_name)
        val driverDe: TextView = itemView.findViewById(R.id.driverType)
        val driverImage: ImageView = itemView.findViewById(R.id.profile_image)

        init {
            itemView.setOnClickListener {
                val (position, user) = it.tag as Pair<Int, cvData>
                listener.clickItem(position, user)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): hold {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.driver_list,parent,false)
        return hold(view,mitem,driverList)
    }

    override fun onBindViewHolder(holder: hold, position: Int) {
        val driver_Detail = driverList[position]
        holder.nam.text = driver_Detail.Name
        holder.driverDe.text = driver_Detail.Position
        Glide.with(holder.itemView.context).load(driver_Detail.Image).into(holder.driverImage)
        holder.itemView.tag = Pair(position, driverList[position])



    }

    override fun getItemCount(): Int {
        return driverList.size
    }
}