package stta.gabriel.ta_gabriel.view.menu.rt.tambahlaporan

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_tambah_laporan.*
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.util.LocationTrack
import stta.gabriel.ta_gabriel.view.menu.rt.kirimlaporan.KirimLaporanActivity


class TambahLaporanFragment : Fragment(), LocationTrack.CallbackonLocChange{
    private var permissionsToRequest = arrayListOf<String>()
    private var permissionsRejected = arrayListOf<String>()
    private var permissions = arrayListOf<String>()
    private lateinit var locationTrack: LocationTrack
    private var long = 0.0
    private var lat = 0.0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tambah_laporan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationTrack = LocationTrack(context, this)
        getLocation(true)

        btntmbh.setOnClickListener (){
            val intent = Intent(activity,KirimLaporanActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onResume() {
        super.onResume()
        getLocation(false)
    }

    override fun onDestroy() {
        locationTrack.stopListener()
        super.onDestroy()
    }

    private fun hasPermission(permission: String): Boolean {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (activity?.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true
    }

    private fun canMakeSmores(): Boolean {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ALL_PERMISSIONS_RESULT -> {
                permissions.forEachIndexed { index, s ->
                    if (!hasPermission(s)) {
                        permissionsRejected.add(s)
                    }
                }

                if (permissionsRejected.size > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel(
                                getString(R.string.grant_access_permission_hint_text),
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(
                                            permissionsRejected.toTypedArray(),
                                            ALL_PERMISSIONS_RESULT
                                        )
                                    }
                                })
                            return
                        }
                    }
                } else getLocation(true)
            }
        }

    }
    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok_text), okListener)
            .setNegativeButton(getString(R.string.cancel_text), null).create().show()
    }


    override fun onChange() {
        getLocation(true)
    }

    private fun getLocation(b: Boolean) {
        initPermissions()
        getLongLat(b)
    }

    private fun initPermissions() {
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissions.add(Manifest.permission.CAMERA)

        permissionsToRequest = findUnAskedPermissions(permissions)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size > 0) {
                requestPermissions(permissionsToRequest.toTypedArray(), ALL_PERMISSIONS_RESULT)
            }
        }
    }

    private fun getLongLat(b: Boolean) {
        if (locationTrack.canGetLocation()) {
            long = locationTrack.longitude
            Log.e("asdfghj", long.toString())
            lat = locationTrack.latitude
            Log.e("asdfghj", lat.toString())
        } else if (b)
            locationTrack.showSettingsAlert()
    }

    private fun findUnAskedPermissions(permissions: ArrayList<String>): ArrayList<String> {
        var result = arrayListOf<String>()
        permissions.forEachIndexed { index, s ->
            if (!hasPermission(s)) {
                result.add(s)
            }
        }
        return result
    }

    companion object {
        const val ALL_PERMISSIONS_RESULT = 999
        fun newInstance() = TambahLaporanFragment()



    }
}

