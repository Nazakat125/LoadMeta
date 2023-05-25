package com.example.ontheroad.driver.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ontheroad.R

class reocrdsAdapter(private val recordList:ArrayList<recordsData>):RecyclerView.Adapter<reocrdsAdapter.viewholder>() {
    class viewholder(view:View):RecyclerView.ViewHolder(view) {
        val fair_amou: TextView = itemView.findViewById(R.id.fair_am)
        val fair_deti: TextView = itemView.findViewById(R.id.fair_de)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {

        val view =LayoutInflater.from(parent.context).inflate(R.layout.fair_records,parent,false)
        return viewholder(view)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
            val data = recordList[position]
            holder.fair_amou.text = data.fair_am
            holder.fair_deti.text = data.fair_de
    }

    override fun getItemCount(): Int {
             return recordList.size
    }
}