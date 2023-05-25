package com.example.ontheroad.owner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ontheroad.R
import com.example.ontheroad.owner.home.HomeAdapter
import com.example.ontheroad.owner.home.home_data
import com.google.firebase.database.FirebaseDatabase

class detailRecords : AppCompatActivity() {
    private lateinit var date:String
    private lateinit var phone:String
    private lateinit var vehical:String
    private lateinit var startDate: TextView
    private lateinit var endDate: TextView
    private lateinit var totalFair: TextView
    private lateinit var totalExpence: TextView
    private lateinit var revenue: TextView
    private lateinit var ExpenceRecy: RecyclerView
    private lateinit var FairRecy: RecyclerView
    private lateinit var list:ArrayList<home_data>
    private lateinit var expnceList:ArrayList<home_data>
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var expenceAdapter: HomeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_records)
        init()
        date =intent.getStringExtra("date").toString()
        phone = intent.getStringExtra("phone").toString()
        vehical = intent.getStringExtra("vehical").toString()
        list = ArrayList()
        expnceList = ArrayList()
        homeAdapter = HomeAdapter(list)
        expenceAdapter = HomeAdapter(expnceList)
        ExpenceRecy.layoutManager  = LinearLayoutManager(this)
        FairRecy.layoutManager = LinearLayoutManager(this)

        val db = FirebaseDatabase.getInstance()
        db.getReference(phone).child(vehical).child("Fair Records").child(date).child("Total").get().addOnSuccessListener {
            val  s_date = it.child("Start Date").getValue().toString()
            val  e_date = it.child("End Date").getValue().toString()
            val o_tot_fair = it.child("Total Fair").getValue().toString()
            val  tot_expence = it.child("Total Expenses").getValue().toString()
            val  rev= it.child("Revenue").getValue().toString()
            startDate.text = s_date
            endDate.text = e_date
            totalFair.text = o_tot_fair
            totalExpence.text = tot_expence
            revenue.text = rev
        }
        db.getReference(phone).child(vehical).child("Fair Records").child(date).child("Fair").get().addOnSuccessListener {
            for (fair in it.children){
                val key = fair.key
                val values = fair.getValue().toString()
                list.add(home_data(key,values))
                FairRecy.adapter = homeAdapter
            }
        }
        db.getReference(phone).child(vehical).child("Fair Records").child(date).child("Expence").get().addOnSuccessListener{
            for (expence in it.children){
                val expenceKey = expence.key.toString()
                val expenceValue = expence.getValue().toString()
                expnceList.add(home_data(expenceKey,expenceValue))
                ExpenceRecy.adapter = expenceAdapter
            }
        }
    }
    fun init(){
        startDate = findViewById(R.id.r_startDate)
        endDate = findViewById(R.id.r_endDate)
        totalFair = findViewById(R.id.r_totalFair)
        totalExpence = findViewById(R.id.r_TotalExpence)
        revenue = findViewById(R.id.r_revenue)
        ExpenceRecy = findViewById(R.id.r_ExpencRecy)
        FairRecy  =findViewById(R.id.r_FairRecy)
    }
}