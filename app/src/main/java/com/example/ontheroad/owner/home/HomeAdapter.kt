package com.example.ontheroad.owner.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ontheroad.R

class HomeAdapter(val homeList:ArrayList<home_data>): RecyclerView.Adapter<HomeAdapter.records>() {
    class records(itemView: View): RecyclerView.ViewHolder(itemView) {
        val h_detail: TextView = itemView.findViewById(R.id.ownerFairDetail)
        val h_amount: TextView = itemView.findViewById(R.id.ownerFairAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): records {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.owner_record, parent, false)
        return records(view)
    }

    override fun onBindViewHolder(holder: records, position: Int) {
        val data = homeList[position]
        holder.h_detail.text = data.detail
        holder.h_amount.text = data.amount

    }

    override fun getItemCount(): Int {
        return homeList.size
    }
}