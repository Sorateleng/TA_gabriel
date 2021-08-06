package stta.gabriel.ta_gabriel.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.model.Akun
import stta.gabriel.ta_gabriel.util.*
import stta.gabriel.ta_gabriel.view.custom.LinearLayoutThatDetectsSoftKeyboard
import stta.gabriel.ta_gabriel.view.menu.rt.HomeRTActivity
import java.util.concurrent.TimeUnit

class RegisterActivity : AppCompatActivity(), LinearLayoutThatDetectsSoftKeyboard.Listener {

    companion object {
        fun start(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, RegisterActivity::class.java))
        }
    }

    private lateinit var callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var resendingToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var verificationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister.setOnClickListener {
            val id = inputId.text.toString()
            val password = inputPassword.text.toString()

            if (inputOtp.text.isNullOrEmpty() && til_otp.isVisible.not()) {
                if (id.isBlank() || password.isBlank()) {
                    toast("Isi data terlebih dahulu")
                } else {
                    initPhoneCallback(id, password)
                    sendOtp(id.getPhoneNumberOnly(), callback)
                    inputId.isEnabled = false
                    inputPassword.isEnabled = false
                    btnRegister.visibility(false)
                    toast("Memproses")
                }
            } else if (inputOtp.text.isNullOrEmpty().not()) {
                btnRegister.visibility(false)
                inputOtp.isEnabled = false
                toast("Memverifikasi")
                val phoneAuthCredential =
                    PhoneAuthProvider.getCredential(verificationId, inputOtp.text.toString())
                signInWithPhoneAuthCredential(phoneAuthCredential, id, password)
            }
        }
    }

    private fun sendOtp(
        phoneNumberOnly: String,
        callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        PhoneAuthProvider.getInstance()
            .verifyPhoneNumber(
                "+62${phoneNumberOnly}",
                2,
                TimeUnit.MINUTES,
                this,
                callback
            )
    }

    override fun onSoftKeyboardShown(isShowing: Boolean) = isShowing.let {
        imageview.visibility(it.not())
        textHintLogin.visibility(it)
    }

    private fun initPhoneCallback(id: String, password: String) {
        callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                if (p0.isNotBlank()) {
                    resendingToken = p1
                    verificationId = p0
                    toast("Mengirim kode verifikasi")

                    btnRegister.visibility(true)
                    til_otp.visibility(true)
                    inputOtp.requestFocus()
                    textView.text = "Verifikasi"
                }
            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(p0, id, password)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                toastFailedOtp("${p0.message}")
            }
        }
    }


    private fun signInWithPhoneAuthCredential(
        p0: PhoneAuthCredential,
        id: String,
        password: String
    ) {
        val authCredential = FirebaseAuth.getInstance()
        authCredential.signInWithCredential(p0).addOnCompleteListener { p0 ->
            if (p0.isSuccessful) {
                val account = Akun(
                    access = ID_RT,
                    nama = id.getPhoneNumberOnly(),
                    head = id.getPhoneNumberOnly().toIntOrNull(),
                    password = password
                )

                val dbReference = FirebaseDatabase.getInstance().reference
                dbReference.child(TABLE_USER).child(id.getPhoneNumberOnly()).setValue(account)
                    .addOnCanceledListener { toastFailedOtp("cancelled reg") }
                    .addOnFailureListener { toastFailedOtp("failed reg") }
                    .addOnSuccessListener {
                        val preferences = SharedPrefs(this)
                        HomeRTActivity.start(this@RegisterActivity)
                        preferences.saveInt(IS_LOGGED_IN, ID_RT)
                        preferences.saveString(USER_VALUE, Gson().toJson(account))
                    }
            } else toastFailedOtp("not success")
        }
    }

    private fun toastFailedOtp(code: String) {
        Log.d("failed_register", code)
        btnRegister.visibility(true)
        inputOtp.isEnabled = true
        toast("Verifikasi gagal, coba kembali\n code: $code ")
    }
}

private fun String.getPhoneNumberOnly(): String {
    return "8${this.substringAfter('8')}"
}
