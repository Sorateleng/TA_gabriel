package stta.gabriel.ta_gabriel.view.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.util.visibility
import stta.gabriel.ta_gabriel.view.custom.LinearLayoutThatDetectsSoftKeyboard

class RegisterActivity : AppCompatActivity(), LinearLayoutThatDetectsSoftKeyboard.Listener {

    companion object{
        fun start(activity: AppCompatActivity){
            activity.startActivity(Intent(activity, RegisterActivity::class.java))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    override fun onSoftKeyboardShown(isShowing: Boolean) = isShowing.let {
        imageview.visibility(it.not())
        textHintLogin.visibility(it)
    }
}