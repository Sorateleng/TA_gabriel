package stta.gabriel.ta_gabriel.view.menu.rt

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_laporan.*
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.base.BaseActivity
import stta.gabriel.ta_gabriel.util.SELECTED_MENU
import stta.gabriel.ta_gabriel.view.menu.rt.tambahlaporan.TambahLaporanFragment
import stta.gabriel.ta_gabriel.view.menu.rt.ulasan.UlasanRTFragment

class HomeRTActivity : BaseActivity() , ActivityCompat.OnRequestPermissionsResultCallback{

    private var lastTab = 0
    var fragment: Fragment? = null

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
        setTitle("Menu RT")
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        savedInstanceState?.getInt(SELECTED_MENU) ?: setNavTab()

        val mFusedLocation = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocation.lastLocation.addOnSuccessListener(this
        ) {
            Log.d("My Current Location", "Lat : ${it?.latitude} Long : ${it?.longitude}")
            Toast.makeText(
                this@HomeRTActivity, "Lat :${it?.latitude} Long : ${it?.longitude} ",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PackageManager.PERMISSION_GRANTED) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(container, R.string.camera_permission_granted, Snackbar.LENGTH_SHORT)

            } else {
                // Permission request was denied.
                Snackbar.make(container, R.string.camera_permission_denied, Snackbar.LENGTH_SHORT)
            }
        }
    }

    private fun setNavTab() {
        navigation.selectedItemId = R.id.menu_tambah_laporan
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
