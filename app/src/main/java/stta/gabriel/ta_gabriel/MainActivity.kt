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

class MainActivity : AppCompatActivity() {
    private lateinit var akun: DatabaseReference
    private lateinit var preferences: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestWindowFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_main)

        preferences = SharedPrefs(this)
        if (preferences.getBoolean(IS_LOGGED_IN)) {
            LaporanActivity.start(this)
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
                                        LaporanActivity.start(this@MainActivity)
                                        preferences.saveBoolean(IS_LOGGED_IN, true)
                                    } else {
                                        toastMe("Password Salah")
                                    }
                                else toastMe("Akun tidak ditemukan")
                            } else toastMe("Akun tidak ditemukan")

                        }
                    }
                })
            }

        }
    }

    private fun toastMe(s: String) {
        Toast.makeText(
            this@MainActivity,
            s,
            Toast.LENGTH_SHORT
        ).show()
    }
}
