package com.example.ontheroad

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class forgotPassword : AppCompatActivity() {
    private lateinit var phoneActivity: LinearLayout
    private lateinit var otpActivity: LinearLayout
    private lateinit var verfiyNumber: EditText
    private lateinit var nextBtn: Button
    private lateinit var exception: TextView
    private lateinit var timer: TextView
    private lateinit var resendBtn: TextView
    private lateinit var verifyphone: TextView
    private lateinit var enterOTP: EditText
    private lateinit var verifyBtn: Button
    private lateinit var phone:String

    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null
    private var mCallBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks?=null
    private var mVarificationId:String? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private var tag = "Main Tag"

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_forgot_password)
        init()
        progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)

        phoneActivity.visibility =View.VISIBLE
        otpActivity.visibility = View.GONE

        firebaseAuth =FirebaseAuth.getInstance()

        mCallBacks = object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                signInWithPhoneAuthCredentia(phoneAuthCredential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                progressDialog.dismiss()
                if (e is FirebaseAuthInvalidCredentialsException) {
                    exception.visibility = View.VISIBLE
                    exception.setText("Invalid OTP")
                } else if (e is FirebaseTooManyRequestsException) {
                    exception.visibility = View.VISIBLE
                    exception.setText("To many Reqest!!Try Again later")
                }
                Toast.makeText(this@forgotPassword, e.message, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID

                mVarificationId  = verificationId
                forceResendingToken = token
                progressDialog.dismiss()
                phoneActivity.visibility = View.GONE
                otpActivity.visibility = View.VISIBLE
                timer()
                Toast.makeText(this@forgotPassword,"Varification code sent", Toast.LENGTH_LONG).show()
                verifyphone.setText("we have send a six digit code to: $phone ")
            }
        }

        nextBtn.setOnClickListener {
            VerifyingPhone()

        }
        resendBtn.setOnClickListener {
            resendVerificationCode(forceResendingToken)
            resendBtn.visibility = View.GONE
            timer()
        }
        verifyBtn.setOnClickListener {
            val code= enterOTP.text.toString()
            if (code.length==6){
                verifyPhoneNumberWithCode(mVarificationId,code)
            }else{
                enterOTP.error = "Enter OTP"
            }
        }
    }
    private fun VerifyingPhone(){
        val phoneNumber = verfiyNumber.text.toString()
        if (phoneNumber.length == 11){
            phone = "+92" + phoneNumber.substring(1)
            val db = Firebase.firestore
            progressDialog.setMessage("Verfiying Phone Number.....")
            progressDialog.show()
            db.collection(phone).document("Profile").get().addOnSuccessListener {
                if (it.exists()){
                    val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBacks!!)          // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                }else{
                    exception.visibility = View.VISIBLE
                    exception.setText("User does not exsist")
                    progressDialog.hide()
                }
            }.addOnFailureListener {
                progressDialog.hide()
            }
        }else{
            exception.visibility = View.VISIBLE
            exception.setText("Invalid Number")
        }
    }

    private fun resendVerificationCode(token:PhoneAuthProvider.ForceResendingToken?){
        progressDialog.setMessage("Resending OTP.....")
        progressDialog.show()
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(mCallBacks!!) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(token!!)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun verifyPhoneNumberWithCode(verificationId:String?,code:String?){
        progressDialog.setMessage("Verfiying OTP.....")
        progressDialog.show()
        val credential = PhoneAuthProvider.getCredential(verificationId!!,code!!)
        signInWithPhoneAuthCredentia(credential)
    }

    private fun signInWithPhoneAuthCredentia(credential: PhoneAuthCredential) {
        progressDialog.setMessage("Logging in.....")
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener {
            val intent = Intent(this@forgotPassword,reCreatePassword::class.java)
            intent.putExtra("phone",phone)
            startActivity(intent)
        }.addOnFailureListener {
            progressDialog.dismiss()
            exception.visibility = View.VISIBLE
            exception.setText("Invalid OTP")
        }
    }

    private fun timer() {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timer.setText("00:" + millisUntilFinished / 1000)
            }

            override fun onFinish() {
                resendBtn.visibility = View.VISIBLE
                timer.visibility = View.INVISIBLE
            }
        }.start()
    }
    fun init(){
        verfiyNumber = findViewById(R.id.f_verifyNumber)
        nextBtn = findViewById(R.id.f_nextBtn)
        exception = findViewById(R.id.f_excetion)
        enterOTP = findViewById(R.id.f_enterOTP)
        verifyBtn = findViewById(R.id.f_verifyBtn)
        verifyphone  = findViewById(R.id.f_verfiyphone)
        phoneActivity = findViewById(R.id.f_phoneActivity)
        otpActivity = findViewById(R.id.f_otpActivity)
        timer = findViewById(R.id.f_timer)
        resendBtn = findViewById(R.id.f_resendBtn)
    }
}