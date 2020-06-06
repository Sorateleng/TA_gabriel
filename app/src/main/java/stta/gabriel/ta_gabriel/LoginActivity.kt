package stta.gabriel.ta_gabriel

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import stta.gabriel.ta_gabriel.model.Akun
import stta.gabriel.ta_gabriel.util.IS_LOGGED_IN
import stta.gabriel.ta_gabriel.util.SharedPrefs

class LoginActivity : AppCompatActivity() {
    private lateinit var akun: DatabaseReference
    private lateinit var preferences: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestWindowFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_main)

        preferences = SharedPrefs(this)
        if (preferences.getInteger(IS_LOGGED_IN) == 1) {
            HomeActivity.start(this)
        } else if (preferences.getInteger(IS_LOGGED_IN) == 2) {
            HomeRTActivity.start(this)
        }

        akun = FirebaseDatabase.getInstance().reference.child("user")

        btnlogin.setOnClickListener {

            val id = inputId.text.toString()
            val password = inputPassword.text.toString()
            if (id.trim().isBlank())
                inputId.error = "Masukkan ID Anda terlebih dahulu"
            else if (password.trim().isBlank())
                inputPassword.error = "Masukkan Password Anda terlebih dahulu"
            else {
                akun.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            Log.e("data1", p0.value.toString())
                            val user = p0.child(inputId.text.toString())
                            if (user.exists()) {
                                val akun = user.getValue<Akun>(Akun::class.java)
                                if (akun != null)
                                    if (akun.password.toString() == inputPassword.text.toString()) {

                                        if (akun.access == 1) {
                                            HomeActivity.start(this@LoginActivity)
                                            preferences.saveInt(IS_LOGGED_IN, 1)
                                        } else if (akun.access == 2) {
                                            HomeRTActivity.start(this@LoginActivity)
                                            preferences.saveInt(IS_LOGGED_IN, 2)
                                        } else {
                                            toastMe("Akun tidak ada akses")
                                        }
                                    } else {
                                        toastMe("Password Salah")
                                    }
                                else toastMe("Akun tidak ditemukan ")
                            } else toastMe("Akun tidak Ada")

                        }
                    }
                })
            }

        }
    }

    private fun toastMe(s: String) {
        Toast.makeText(
            this@LoginActivity,
            s,
            Toast.LENGTH_SHORT
        ).show()
    }
}
