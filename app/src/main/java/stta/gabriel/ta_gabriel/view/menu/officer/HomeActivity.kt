package stta.gabriel.ta_gabriel.view.menu.officer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_laporan.*
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.base.BaseActivity
import stta.gabriel.ta_gabriel.model.Akun
import stta.gabriel.ta_gabriel.util.*
import stta.gabriel.ta_gabriel.view.auth.LoginActivity
import stta.gabriel.ta_gabriel.view.menu.officer.laporan.LaporanFragment
import stta.gabriel.ta_gabriel.view.menu.officer.riwayat.RiwayatFragment
import stta.gabriel.ta_gabriel.view.menu.officer.ulasan.UlasanFragment

class HomeActivity : BaseActivity() {

    private var lastTab = 0
    var fragment: Fragment? = null
    private lateinit var preferences: SharedPrefs
    lateinit var akun: Akun

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            if (item.itemId != lastTab)
                when (item.itemId) {
                    R.id.menu_laporan -> {
                        fragment = LaporanFragment.newInstance()
                    }
                    R.id.menu_riwayat -> {
                        fragment = RiwayatFragment.newInstance()
                    }
                    R.id.menu_ulasan -> {
                        fragment = UlasanFragment.newInstance()
                    }
                }
            if (fragment != null) {
                lastTab = item.itemId
                supportFragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.container, fragment!!)
                    .commit()
            }
            true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan)
        setTitle(getString(R.string.officer_menu_title_text))
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        savedInstanceState?.getInt(SELECTED_MENU) ?: setNavTab()
        preferences = SharedPrefs(this)
        akun = Gson().fromJson(preferences.getString(USER_VALUE), Akun::class.java)
    }

    private fun setNavTab() {
        navigation.selectedItemId = R.id.menu_laporan
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_MENU, navigation.selectedItemId)
    }

    override fun onBackPressed() {
        showDialog(
            cancelable = false,
            cancelableTouchOutside = true
        ) {
            setTitle(getString(R.string.exit_title_text))
            positiveButton(getString(R.string.logout_text)) {
                preferences.saveInt(IS_LOGGED_IN, 0)
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                finishAffinity()
            }
            negativeButton(getString(R.string.exit_text)) {
                super.onBackPressed()
            }
            neutralButton(getString(R.string.cancel_text)) {

            }
        }
    }


    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, HomeActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }
}
