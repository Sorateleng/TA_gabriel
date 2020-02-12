package stta.gabriel.ta_gabriel

import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestWindowFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_main)

        btnlogin.setOnClickListener {
            val id = inputId.text.toString()
            val password = inputPassword.text.toString()
            if (id.trim().isNullOrBlank())
                inputId.error = "Masukkan ID Anda terlebih dahulu"
            else if (password.trim().isNullOrBlank())
                inputPassword.error = "Masukkan Password Anda terlebih dahulu"
            else Toast.makeText(
                this,
                "ID Anda : $id \nPassword Anda : $password",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
