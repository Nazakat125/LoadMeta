package com.example.ontheroad.driver.home

import android.app.ActionBar
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ontheroad.R
import com.google.firebase.database.FirebaseDatabase

class driverHome : Fragment() {
    private lateinit var s_data_btn:Button
    private lateinit var e_data_btn:Button
    private lateinit var s_data:TextView
    private lateinit var e_data:TextView
    private lateinit var clander:Dialog
    private lateinit var cla:CalendarView
    private lateinit var fairExpance:AutoCompleteTextView
    private lateinit var fairCon:LinearLayout
    private lateinit var expanceCon:LinearLayout
    private lateinit var fairRecy:LinearLayout
    private lateinit var expanceRecy:LinearLayout
    private lateinit var fairDetail:EditText
    private lateinit var expanceDetail:EditText
    private lateinit var fairAmount:EditText
    private lateinit var expanceAmount:EditText
    private lateinit var fairAddBtn:Button
    private lateinit var expanceAddBtn:Button
    private lateinit var fairRecyclerView: RecyclerView
    private lateinit var expanceRecyclerView: RecyclerView
    private lateinit var fairList:ArrayList<recordsData>
    private lateinit var expanceList:ArrayList<recordsData>
    private lateinit var doneBtn:Button
    private lateinit var totalDialog: Dialog
    private lateinit var totalFair:TextView
    private lateinit var totalExpance:TextView
    private lateinit var revenue:TextView
    private lateinit var totalcheck:CheckBox
    private lateinit var totalCancleBtn:Button
    private lateinit var totalShareBtn:Button
    private lateinit var progressDialog:ProgressDialog


    private lateinit var startDate:String
    private lateinit var endDate:String
    private lateinit var phone:String
    private lateinit var vehicle:String
                     var t_fair:Int = 0
                     var t_expence:Int = 0
                     var t_revenue:Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_driver_home, container, false)
        phone = arguments?.getString("phone").toString()
        vehicle = arguments?.getString("vehicle").toString()
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setCanceledOnTouchOutside(false)

        clander = Dialog(requireContext())
        clander.setContentView(R.layout.date)
        clander.setCanceledOnTouchOutside(false)

        totalDialog = Dialog(requireContext())
        totalDialog.setContentView(R.layout.final_record)
        totalDialog.setCanceledOnTouchOutside(false)
        init(view)
        val window:Window? =clander.window
        window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.WRAP_CONTENT)
        
        s_data_btn.setOnClickListener { 
            clander.show()
            cla.setOnDateChangeListener { view, year, month, dayOfMonth ->

                startDate = "$dayOfMonth-${month.toString().toInt() + 1}-$year"
                s_data.visibility = View.VISIBLE
                s_data.setText(startDate)
                clander.dismiss()
            }

        }
        
        e_data_btn.setOnClickListener { 
            clander.show()
            cla.setOnDateChangeListener { view, year, month, dayOfMonth ->
                endDate = "$dayOfMonth-${month.toString().toInt() + 1}-$year"
                e_data.visibility = View.VISIBLE
                e_data.setText(endDate)
                clander.dismiss()
            }
        }


        val f_e_list = arrayListOf("Fair","Expance")
        val fairExpanceAdapter  = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,f_e_list)
        fairExpance.setAdapter(fairExpanceAdapter)
        fairExpance.setOnItemClickListener { parent, view, position, id ->

            val select = parent.getItemAtPosition(position) as String

            if (select == null){
                fairExpance.error = "Select some item"
            }else{

                if (select == "Fair"){
                    expanceCon.visibility = View.GONE
                    fairCon.visibility = View.VISIBLE
                    fairRecyclerView.layoutManager =LinearLayoutManager(requireContext())
                    fairList = arrayListOf()
                    fairAddBtn.setOnClickListener {
                        val f_detail = fairDetail.text.toString()
                        val f_amount = fairAmount.text.toString()
                       if (f_detail.isNotEmpty() && f_amount.isNotEmpty()){
                            fairList.add(recordsData(f_amount,f_detail))
                            val f_adapter =reocrdsAdapter(fairList)
                            fairRecyclerView.adapter = f_adapter
                            fairRecy.visibility = View.VISIBLE
                            fairDetail.text.clear()
                            fairAmount.text.clear()
                           t_fair = 0
                           for (tot in fairList){
                               t_fair += tot.fair_am.toInt()
                           }


                        }else{
                            if (f_detail.isEmpty()){
                                fairDetail.error = "Enter Fair Detail"
                            }
                            if (f_amount.isEmpty()){
                                fairAmount.error = "Enter Fair Amount"
                            }
                        }
                    }
                    

                }
                else if (select == "Expance"){
                    fairCon.visibility = View.GONE
                    expanceCon.visibility =View.VISIBLE
                    expanceRecyclerView.layoutManager =LinearLayoutManager(requireContext())
                    expanceList = arrayListOf()
                    expanceAddBtn.setOnClickListener {
                        val e_detail = expanceDetail.text.toString()
                        val e_amount = expanceAmount.text.toString()
                        if (e_detail.isNotEmpty() && e_amount.isNotEmpty()){
                            expanceList.add(recordsData(e_amount,e_detail))
                            val e_adapter =reocrdsAdapter(expanceList)
                            expanceRecyclerView.adapter = e_adapter
                            expanceRecy.visibility = View.VISIBLE
                            expanceDetail.text.clear()
                            expanceAmount.text.clear()
                            t_expence = 0
                            for (tot_e in expanceList){
                                t_expence += tot_e.fair_am.toInt()
                            }

                        }else{
                            if (e_detail.isEmpty()){
                                expanceDetail.error = "Enter Fair Detail"
                            }
                            if (e_amount.isEmpty()){
                                expanceAmount.error = "Enter Fair Amount"
                            }
                        }
                    }



                }
            }


        }

        val Totalwindow:Window? =totalDialog.window
        Totalwindow?.setLayout(ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.WRAP_CONTENT)
        doneBtn.setOnClickListener {
            totalDialog.show()
            t_revenue = t_fair - t_expence
            totalFair.text = t_fair.toString()
            totalExpance.text = t_expence.toString()
            if (t_revenue <= 0){
                revenue.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))
            }else{
                revenue.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
            revenue.text = t_revenue.toString()

            val db = FirebaseDatabase.getInstance()
            val ref = db.getReference(phone).child(vehicle).child("Records").child("Driver Records").child(startDate)

            totalCancleBtn.setOnClickListener {
                totalDialog.dismiss()

            }
            totalcheck.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked == true){
                    totalShareBtn.visibility = View.VISIBLE
                    totalShareBtn.setOnClickListener {
                        val t_data = hashMapOf<String,Any>()
                        val fair = hashMapOf<String,Any>()
                        val expence = hashMapOf<String,Any>()
                        for (f in fairList){
                            val f_am = f.fair_am
                            val f_de = f.fair_de
                            fair[f_de] = f_am
                        }
                        for (e in expanceList){
                            val e_am = e.fair_am
                            val e_de = e.fair_de
                            expence[e_de] = e_am
                        }

                        t_data["Start Date"] =startDate
                        t_data["End Date"] = endDate
                        t_data["Total Fair"] = t_fair.toString()
                        t_data["Total Expenses"] =t_expence.toString()
                        t_data["Revenue"] = t_revenue.toString()
                        progressDialog.setMessage("Please Wait......")
                        progressDialog.show()

                        ref.child("Fair").setValue(fair).addOnSuccessListener {
                            progressDialog.dismiss()
                            totalDialog.dismiss()
                            val intent = requireActivity().intent
                            requireActivity().finish()
                            requireActivity().startActivity(intent)
                            startActivity(intent)
                        }.addOnFailureListener {
                            Toast.makeText(requireContext(),"$it",Toast.LENGTH_LONG).show()
                        }
                        ref.child("Expence").setValue(expence)
                        ref.child("Total").setValue(t_data)



                    }
                }else{
                    totalShareBtn.visibility = View.GONE
                }
            }

        }



        return view
    }

    private fun init(view:View){
        s_data_btn = view.findViewById(R.id.s_date)
        s_data = view.findViewById(R.id.s_selected_date)
        e_data_btn = view.findViewById(R.id.e_date)
        e_data = view.findViewById(R.id.e_selected_date)
        cla = clander.findViewById(R.id.calendarView)
        fairExpance = view.findViewById(R.id.fairandexpance)
        fairCon = view.findViewById(R.id.fair_con)
        expanceCon = view.findViewById(R.id.expence_con)
        fairDetail = view.findViewById(R.id.fair_detail)
        expanceDetail = view.findViewById(R.id.e_detail)
        fairAmount = view.findViewById(R.id.fair_amount)
        expanceAmount = view.findViewById(R.id.expence_amount)
        fairAddBtn = view.findViewById(R.id.fairAdd)
        expanceAddBtn = view.findViewById(R.id.expanceAddBtn)
        fairRecyclerView = view.findViewById(R.id.fair_recyView)
        fairRecy= view.findViewById(R.id.fair_list)
        expanceRecy = view.findViewById(R.id.expence_list)
        expanceRecyclerView = view.findViewById(R.id.expence_recyView)
        doneBtn = view.findViewById(R.id.recordDone)
        doneBtn = view.findViewById(R.id.recordDone)
        totalFair = totalDialog.findViewById(R.id.totalFair)
        totalExpance = totalDialog.findViewById(R.id.totalExpence)
        revenue = totalDialog.findViewById(R.id.income)
        totalcheck = totalDialog.findViewById(R.id.totalCheck)
        totalCancleBtn = totalDialog.findViewById(R.id.driver_cancel)
        totalShareBtn = totalDialog.findViewById(R.id.driver_share)
    }
}