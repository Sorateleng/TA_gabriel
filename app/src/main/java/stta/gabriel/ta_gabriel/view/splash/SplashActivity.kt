package stta.gabriel.ta_gabriel.view.splash

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.view.auth.LoginActivity
import stta.gabriel.ta_gabriel.view.menu.officer.HomeActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()
        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity,LoginActivity::class.java))
            finish()
        },5000)
    }
}