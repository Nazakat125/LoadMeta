package com.example.ontheroad.owner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ontheroad.R

class recordsAdapter(private var recordsList: ArrayList<recordsData>) :
    RecyclerView.Adapter<recordsAdapter.records>() {

    private lateinit var mclick: recodsClick

    interface recodsClick {
        fun clikRecords(position: Int, user: recordsData)
    }

    fun recodsClick(listener: recodsClick) {
        mclick = listener
    }

    inner class records(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recordDate: TextView = itemView.findViewById(R.id.recodsDate)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val user = recordsList[position]
                    mclick.clikRecords(position, user)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): records {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.record_recy_list, parent, false)
        return records(view)
    }

    override fun onBindViewHolder(holder: records, position: Int) {
        val viewRecords = recordsList[position]
        holder.recordDate.text = viewRecords.records
        holder.itemView.tag = Pair(position, viewRecords)
    }

    override fun getItemCount(): Int {
        return recordsList.size
    }
}