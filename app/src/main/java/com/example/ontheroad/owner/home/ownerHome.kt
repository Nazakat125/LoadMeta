package com.example.ontheroad.owner.home

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ontheroad.R
import com.google.firebase.database.*

class ownerHome : Fragment() {


    private lateinit var phone:String
    private lateinit var vehical:String
    private lateinit var ExpenceRecy: RecyclerView
    private lateinit var FairRecy: RecyclerView
    private lateinit var Deletebtn: Button
    private lateinit var Donebtn: Button
    private lateinit var startDate: TextView
    private lateinit var endDate: TextView
    private lateinit var totalFair: TextView
    private lateinit var totalExpence: TextView
    private lateinit var revenue: TextView
    private lateinit var layout: LinearLayout
    private lateinit var list:ArrayList<home_data>
    private lateinit var expnceList:ArrayList<home_data>
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var expenceAdapter: HomeAdapter
    private lateinit var deleteDialog: Dialog
    private lateinit var doneDialog: Dialog
    private lateinit var e_date:String
    private lateinit var s_date:String
    private lateinit var o_tot_fair:String
    private lateinit var tot_expence:String
    private lateinit var rev:String
    private lateinit var totalRecord: AutoCompleteTextView
    private lateinit var progressDialog: ProgressDialog




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_owner_home, container, false)
        init(view)
        phone = arguments?.getString("phone").toString()
        vehical = arguments?.getString("vehicle").toString()
        ExpenceRecy.layoutManager  = LinearLayoutManager(requireContext())
        FairRecy.layoutManager = LinearLayoutManager(requireContext())
        list = ArrayList()

        val db = FirebaseDatabase.getInstance()

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage("Please Wait......")
        deleteDialog = Dialog(requireContext())
        doneDialog = Dialog(requireContext())
        deleteDialog.setContentView(R.layout.owner_canncel_btn)
        doneDialog.setContentView(R.layout.owner_done_btn)
        deleteDialog.setCanceledOnTouchOutside(false)
        doneDialog.setCanceledOnTouchOutside(false)
        expnceList = ArrayList()
        homeAdapter = HomeAdapter(list)
        expenceAdapter = HomeAdapter(expnceList)
        layout.visibility = View.GONE


        progressDialog.show()
        val totalAmountref = db.getReference(phone).child(vehical).child("Records").child("Final Income")
        totalAmountref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    progressDialog.dismiss()
                    val currenttotal = snapshot.child("Income").getValue().toString()
                    if (currenttotal <= 0.toString()){
                        totalRecord.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))
                    }else{
                        totalRecord.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                    }
                    totalRecord.setText(currenttotal)
                }else{
                    progressDialog.dismiss()
                }


            }

            override fun onCancelled(error: DatabaseError) {
            progressDialog.dismiss()
                Toast.makeText(requireContext(),"$error",Toast.LENGTH_LONG).show()

            }

        })


       val ref =  db.getReference(phone).child(vehical).child("Records").child("Driver Records")

        ref.limitToLast(1).addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                progressDialog.dismiss()
                list.clear()
                layout.visibility = View.VISIBLE
                if (snapshot.exists()) {
                s_date = snapshot.key.toString()

                ref.child(s_date).child("Fair").get().addOnSuccessListener {
                    for (fair in it.children) {
                        val key = fair.key
                        val values = fair.getValue().toString()
                        list.add(home_data(key, values))
                        FairRecy.adapter = homeAdapter
                    }
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()
                }

                ref.child(s_date).child("Expence").get().addOnSuccessListener {
                    for (expence in it.children) {
                        val expenceKey = expence.key.toString()
                        val expenceValue = expence.getValue().toString()
                        expnceList.add(home_data(expenceKey, expenceValue))
                        ExpenceRecy.adapter = expenceAdapter
                    }

                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()
                }

                ref.child(s_date).child("Total").get().addOnSuccessListener {
                    s_date = it.child("Start Date").getValue().toString()
                    e_date = it.child("End Date").getValue().toString()
                    o_tot_fair = it.child("Total Fair").getValue().toString()
                    tot_expence = it.child("Total Expenses").getValue().toString()
                    rev = it.child("Revenue").getValue().toString()
                    startDate.text = s_date
                    endDate.text = e_date
                    totalFair.text = o_tot_fair
                    totalExpence.text = tot_expence
                    if (rev <= 0.toString()) {
                        revenue.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                    }else{
                        revenue.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    }
                    revenue.text = rev

                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()
                }

                Deletebtn.setOnClickListener {
                    deleteDialog.show()
                    val del_cancel = deleteDialog.findViewById<Button>(R.id.owner_delte_canncel)
                    val del_delete = deleteDialog.findViewById<Button>(R.id.owner_delte_Delete)
                    del_cancel.setOnClickListener {
                        deleteDialog.hide()
                    }
                    del_delete.setOnClickListener {
                        progressDialog.show()
                        ref.child(s_date).removeValue().addOnSuccessListener {
                            progressDialog.dismiss()
                            layout.visibility = View.GONE
                            deleteDialog.hide()
                            Toast.makeText(requireContext(), "Records Deleted", Toast.LENGTH_LONG)
                                .show()
                        }.addOnFailureListener {
                            Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()
                            progressDialog.dismiss()
                        }

                    }
                }
                Donebtn.setOnClickListener {


                    val totalAmountref =
                        db.getReference(phone).child(vehical).child("Records").child("Final Income")
                    totalAmountref.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                val currenttotal =
                                    snapshot.child("Income").getValue().toString().toInt()
                                val newtotal = currenttotal + rev.toInt()
                                totalRecord.setText(newtotal.toString())
                                val setvalues = hashMapOf("Income" to newtotal)
                                if (newtotal <= 0) {
                                    totalRecord.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                                }else{
                                    totalRecord.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                                }
                                totalAmountref.setValue(setvalues)

                            } else {
                                totalRecord.setText(rev)
                                if (rev <= 0.toString()) {
                                    totalRecord.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                                }else{
                                    totalRecord.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                                }
                                val setvalues = hashMapOf("Income" to rev)
                                totalAmountref.setValue(setvalues)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                            progressDialog.dismiss()
                            Toast.makeText(requireContext(), "$error", Toast.LENGTH_LONG).show()
                        }

                    })









                    doneDialog.show()
                    val done_cancel = doneDialog.findViewById<Button>(R.id.owner_done_canncel)
                    val done_done = doneDialog.findViewById<Button>(R.id.owner_done_Done)
                    done_cancel.setOnClickListener {
                        doneDialog.hide()
                    }

                    done_done.setOnClickListener {
                        progressDialog.show()
                        val nextref = db.getReference(phone).child(vehical).child("Fair Records")
                            .child(s_date)
                        val t_data = hashMapOf<String, Any>()
                        val fair = hashMapOf<String, Any>()
                        val expence = hashMapOf<String, Any>()

                        for (f_data in list) {
                            val fair_amount = f_data.amount.toString()
                            val fair_detai = f_data.detail.toString()
                            fair[fair_detai] = fair_amount
                        }
                        for (e_data in expnceList) {
                            val ex_amount = e_data.amount.toString()
                            val ex_detai = e_data.detail.toString()
                            expence[ex_detai] = ex_amount
                        }

                        t_data["Start Date"] = s_date
                        t_data["End Date"] = e_date
                        t_data["Total Fair"] = o_tot_fair
                        t_data["Total Expenses"] = tot_expence
                        t_data["Revenue"] = rev
                        nextref.child("Total").setValue(t_data)
                        nextref.child("Fair").setValue(fair)
                        nextref.child("Expence").setValue(expence).addOnSuccessListener {
                            Toast.makeText(requireContext(), "Records Updated", Toast.LENGTH_LONG)
                                .show()
                            progressDialog.dismiss()
                            ref.child(s_date).removeValue()
                            layout.visibility = View.GONE
                            doneDialog.hide()
                        }.addOnFailureListener {
                            Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()
                            progressDialog.dismiss()
                        }


                    }


                }

            }else{
               progressDialog.dismiss()
            }
            }



            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {
                progressDialog.dismiss()
            }

        })







        return view
    }

    fun init(view: View){
        ExpenceRecy = view.findViewById(R.id.o_ExpencRecy)
        FairRecy  = view.findViewById(R.id.o_FairRecy)
        Deletebtn = view.findViewById(R.id.o_delete)
        Donebtn = view.findViewById(R.id.o_Update)
        layout = view.findViewById(R.id.newRecord)
        startDate = view.findViewById(R.id.o_startDate)
        endDate = view.findViewById(R.id.o_endDate)
        totalFair = view.findViewById(R.id.o_totalFair)
        totalExpence = view.findViewById(R.id.o_TotalExpence)
        revenue = view.findViewById(R.id.o_revenue)
        totalRecord  = view.findViewById(R.id.OwnerTota)
    }

}