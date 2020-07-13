package stta.gabriel.ta_gabriel.view.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_laporan.*
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.view.menu.rt.tambahlaporan.TambahLaporanFragment
import stta.gabriel.ta_gabriel.view.menu.rt.ulasan.UlasanRTFragment

class HomeRTActivity : AppCompatActivity() {

    private var lastTab = 0
    var fragment: Fragment? = null
    private val SELECTED_MENU = "selected_menu"

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            if (item.itemId != lastTab)
                if (item.itemId == R.id.menu_tambah_laporan) {
                    fragment = TambahLaporanFragment.newInstance()
                } else if (item.itemId == R.id.menu_ulasan_rt) {
                    fragment = UlasanRTFragment.newInstance()
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
        setContentView(R.layout.activity_menu_rt)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        savedInstanceState?.getInt(SELECTED_MENU) ?: setNavTab()
    }

    private fun setNavTab() {
        navigation.selectedItemId =
            R.id.menu_tambah_laporan
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_MENU, navigation.selectedItemId)
    }


    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, HomeRTActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }
}
