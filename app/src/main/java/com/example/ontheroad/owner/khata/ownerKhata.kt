package com.example.ontheroad.owner.khata

import android.app.ActionBar
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ontheroad.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ownerKhata : Fragment() {

    private lateinit var btnDialog: Dialog
    private lateinit var phone:String
    private lateinit var vehical:String
    private lateinit var kahttaAdapter: kahttaAdapter
    private lateinit var list:ArrayList<kahttaData>
    private lateinit var kahttaRecy: RecyclerView
    private lateinit var progressDialog: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      val view = inflater.inflate(R.layout.fragment_owner_khata, container, false)

        btnDialog = Dialog(requireContext())
        btnDialog.setContentView(R.layout.kahtta_pop_up)
        btnDialog.setCanceledOnTouchOutside(false)
        kahttaRecy  = view.findViewById(R.id.kahttaRecy)
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage("Please Wait ........")
        val window: Window? = btnDialog.getWindow()
        window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
        val addBtn = view.findViewById<ImageButton>(R.id.expeceAddBtn)
        phone = arguments?.getString("phone").toString()
        vehical = arguments?.getString("vehicle").toString()

        list  = ArrayList()
        kahttaAdapter = kahttaAdapter(list)
        kahttaRecy.layoutManager = LinearLayoutManager(requireContext())
        val db  = FirebaseDatabase.getInstance()
        progressDialog.show()
        val getref =db.getReference(phone).child(vehical).child("Records").child("Expence Records")
        getref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                progressDialog.dismiss()
                for (ds in snapshot.children) {
                    val xDetail = ds.child("Detail").value.toString()
                    val xAmount = ds.child("Amount").value.toString()
                    val xTime = ds.child("Current TIme").value.toString()
                    val xDate = ds.child("Current Date").value.toString()
                    list.add(kahttaData(xDate,xDetail,xAmount,xTime))
                }
                kahttaAdapter.notifyDataSetChanged()
                kahttaRecy.adapter = kahttaAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"$error",Toast.LENGTH_LONG).show()
                progressDialog.dismiss()
            }

        })


        addBtn.setOnClickListener {
            btnDialog.show()
            val expenceDetail = btnDialog.findViewById<EditText>(R.id.expenceDe)
            val expenceAmount = btnDialog.findViewById<EditText>(R.id.expenceAm)
            val expenceCheck = btnDialog.findViewById<CheckBox>(R.id.expence_check)
            val expenceAddBtn = btnDialog.findViewById<Button>(R.id.expenceAdd)
            val expenceCancel = btnDialog.findViewById<Button>(R.id.expenceCancel)
            expenceCancel.setOnClickListener {
                btnDialog.hide()
            }
            expenceCheck.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    expenceAddBtn.visibility = View.VISIBLE
                    expenceAddBtn.setOnClickListener {
                        val exDetail = expenceDetail.text.toString().toUpperCase()
                        val exAmount = expenceAmount.text.toString()
                        if (exDetail.isNotEmpty()&& exAmount.isNotEmpty()){
                            val db = FirebaseDatabase.getInstance()
                            val currentTime = Calendar.getInstance().time
                            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

                            val formattedDate = dateFormat.format(currentTime)
                            val formattedTime = timeFormat.format(currentTime)
                            val ref =db.getReference(phone).child(vehical).child("Records").child("Expence Records").push()
                            val expence = hashMapOf(
                                "Detail" to exDetail,
                                "Amount" to exAmount,
                                "Current TIme" to formattedTime,
                                "Current Date" to formattedDate)
                            progressDialog.show()
                            ref.setValue(expence).addOnSuccessListener {
                                progressDialog.dismiss()
                                expenceAmount.text.clear()
                                expenceDetail.text.clear()
                                btnDialog.hide()
                                val newref = db.getReference(phone).child(vehical).child("Records").child("Final Income")
                                newref.get().addOnSuccessListener {
                                    if (it.exists()){
                                        val totalAmount = it.child("Income").getValue().toString().toInt()
                                        val newtotal =totalAmount - exAmount.toInt()
                                        val updateValues = mapOf<String, Any>(
                                            "Income" to newtotal)
                                        newref.updateChildren(updateValues)
                                            .addOnSuccessListener {
                                                kahttaRecy.adapter = kahttaAdapter }
                                    }else{
                                        val vales = "-$exAmount"
                                        ref.setValue(vales).addOnSuccessListener {
                                            kahttaRecy.adapter = kahttaAdapter
                                            progressDialog.dismiss()
                                        }.addOnFailureListener{
                                            Toast.makeText(requireContext(),"$it",Toast.LENGTH_LONG).show()
                                            progressDialog.dismiss()
                                        }
                                    }
                                }.addOnFailureListener {
                                    Toast.makeText(requireContext(),"$it",Toast.LENGTH_LONG).show()
                                    progressDialog.dismiss()
                                }
                            }.addOnFailureListener {
                                Toast.makeText(requireContext(),"$it",Toast.LENGTH_LONG).show()
                                progressDialog.dismiss()
                            }




                        }else{
                            if (exDetail.isEmpty()){
                                expenceDetail.error = ""
                            }
                            if (exAmount.isEmpty()){
                                expenceAmount.error = ""
                            }

                        }

                    }
                }else{
                    expenceAddBtn.visibility = View.GONE
                }
            }

        }

      return view
    }

}