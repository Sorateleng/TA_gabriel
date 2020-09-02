package stta.gabriel.ta_gabriel.view.menu.rt.tambahlaporan

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_tambah_laporan.*
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.model.Akun
import stta.gabriel.ta_gabriel.util.*
import stta.gabriel.ta_gabriel.view.menu.rt.kirimlaporan.KirimLaporanActivity
import java.io.File


class TambahLaporanFragment : Fragment(), LocationTrack.CallbackonLocChange,
    UploadImage.CallbackUploadImageUrl {
    private var permissionsToRequest = arrayListOf<String>()
    private var permissionsRejected = arrayListOf<String>()
    private var permissions = arrayListOf<String>()
    private lateinit var locationTrack: LocationTrack
    private var long = 0.0
    private var lat = 0.0

    private lateinit var prefs: SharedPrefs
    private lateinit var akun: Akun

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tambah_laporan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = SharedPrefs(requireContext())
        akun = Gson().fromJson(prefs.getString(USER_VALUE), Akun::class.java)

        locationTrack = LocationTrack(context, this)
        getLocation(true)
        flipper.displayedChild = 0

        btntmbh.setOnClickListener {
            openCamera()
        }
    }

    private fun openCamera() {
        val option = Options.init()
            .setRequestCode(CAMERA_REQ_CODE)
            .setCount(1)
            .setFrontfacing(false)
            .setExcludeVideos(true)
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)
            .setPath("/PoncoWarno/images")
        Pix.start(this, option)
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
                return (activity?.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)
            }
        }
        return true
    }

    private fun canMakeSmores(): Boolean {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
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
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(
                        context,
                        "Approve permissions to open Pix ImagePicker",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQ_CODE) {
            flipper.displayedChild = 1
            val img = data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
            val photoFile = File("${img?.get(0)}")
            UploadImage(
                activity,
                akun.head.toString(),
                photoFile.name,
                photoFile,
                this,
                DIR_ITEM_IMG
            ).upload(ACCESS_CAM)
        }
    }

    override fun imageUrl(imgUrl: String?, error: String?, access: Int) {
        flipper.displayedChild = 0
        if (imgUrl.isNullOrEmpty() && error != null) {
            Toast.makeText(context, getString(R.string.failed_upload_warn_text), Toast.LENGTH_SHORT)
                .show()
        } else {
            val intent = Intent(activity, KirimLaporanActivity::class.java)
            intent.putExtra(IMAGE_EXTRA, imgUrl)
            intent.putExtra(LONG_EXTRA, long)
            intent.putExtra(LAT_EXTRA, lat)
            startActivity(intent)
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
        private const val CAMERA_REQ_CODE = 666
        private const val ACCESS_CAM = 777
        const val IMAGE_EXTRA = "IMAGE_EXTRA"
        const val LONG_EXTRA = "LONG_EXTRA"
        const val LAT_EXTRA = "LAT_EXTRA"
        fun newInstance() = TambahLaporanFragment()


    }
}

