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
            LaporanActivity.start(this@MainActivity)
        }
    }
}
