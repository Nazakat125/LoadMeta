package com.example.ontheroad.owner.khata

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ontheroad.R

class kahttaAdapter(val kahttaList:ArrayList<kahttaData>): RecyclerView.Adapter<kahttaAdapter.kahtta>() {
    class kahtta(val view: View): RecyclerView.ViewHolder(view) {
        val expenceDate: TextView = itemView.findViewById(R.id.exDate)
        val expenceDetail: TextView = itemView.findViewById(R.id.exDetail)
        val expenceAmount: TextView = itemView.findViewById(R.id.expenceAmountt)
        val expenceTime: TextView = itemView.findViewById(R.id.exTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): kahtta {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.add_expence, parent, false)
        return kahtta(view)
    }

    override fun onBindViewHolder(holder: kahtta, position: Int) {
        val data = kahttaList[position]
        holder.expenceDate.text = data.expenceDate
        holder.expenceDetail.text = data.expenceDetail
        holder.expenceAmount.text = data.expenceAmount
        holder.expenceTime.text = data.expenceTime
    }

    override fun getItemCount(): Int {
        return kahttaList.size
    }
}