package stta.gabriel.ta_gabriel.view.auth

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.model.Akun
import stta.gabriel.ta_gabriel.util.*
import stta.gabriel.ta_gabriel.view.menu.officer.HomeActivity
import stta.gabriel.ta_gabriel.view.menu.rt.HomeRTActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var akun: DatabaseReference
    private lateinit var preferences: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestWindowFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_main)

        preferences = SharedPrefs(this)
        when {
            preferences.getInteger(IS_LOGGED_IN) == ID_OFFICER -> {
                HomeActivity.start(this)
            }
            preferences.getInteger(IS_LOGGED_IN) == ID_RT -> {
                HomeRTActivity.start(this)
            }
            else -> {
                setLoading(false)
            }
        }

        akun = FirebaseDatabase.getInstance().reference.child(TABLE_USER)

        btnlogin.setOnClickListener {
            setLoading(true)
            val id = inputId.text.toString()
            val password = inputPassword.text.toString()
            when {
                id.trim().isBlank() -> inputId.error = "Masukkan ID Anda terlebih dahulu"
                password.trim().isBlank() -> inputPassword.error =
                    "Masukkan Password Anda terlebih dahulu"
                else -> {
                    akun.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            setLoading(false)
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()) {
                                Log.e("data1", p0.value.toString())
                                val user = p0.child(inputId.text.toString())
                                if (user.exists()) {
                                    val akun = user.getValue<Akun>(Akun::class.java)
                                    if (akun != null)
                                        if (akun.password.toString() == inputPassword.text.toString()) {

                                            when (akun.access) {
                                                ID_OFFICER -> {
                                                    HomeActivity.start(this@LoginActivity)
                                                    preferences.saveInt(IS_LOGGED_IN, ID_OFFICER)
                                                    preferences.saveString(
                                                        USER_VALUE,
                                                        Gson().toJson(akun)
                                                    )
                                                }
                                                ID_RT -> {
                                                    HomeRTActivity.start(this@LoginActivity)
                                                    preferences.saveInt(IS_LOGGED_IN, ID_RT)
                                                    preferences.saveString(
                                                        USER_VALUE,
                                                        Gson().toJson(akun)
                                                    )
                                                }
                                                else -> {
                                                    toastMe("Akun tidak ada akses")
                                                }
                                            }
                                        } else {
                                            toastMe("Password Salah")
                                        }
                                    else toastMe("Akun tidak ditemukan ")
                                } else toastMe("Akun tidak Ada")

                            }
                            setLoading(false)
                        }
                    })
                }
            }

        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            progressLay.setVisible()
            btnlogin.setGone()
        } else {
            progressLay.setGone()
            btnlogin.setVisible()
        }
    }

    private fun toastMe(s: String) {
        Toast.makeText(this@LoginActivity, s, Toast.LENGTH_SHORT).show()
    }
}
