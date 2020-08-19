package stta.gabriel.ta_gabriel.view.auth

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.model.Akun
import stta.gabriel.ta_gabriel.util.*
import stta.gabriel.ta_gabriel.view.custom.LinearLayoutThatDetectsSoftKeyboard
import stta.gabriel.ta_gabriel.view.menu.officer.HomeActivity
import stta.gabriel.ta_gabriel.view.menu.rt.HomeRTActivity
import stta.gabriel.ta_gabriel.view.menu.rt.tambahlaporan.TambahLaporanFragment


class LoginActivity : AppCompatActivity(), LinearLayoutThatDetectsSoftKeyboard.Listener {
    private lateinit var akun: DatabaseReference
    private lateinit var preferences: SharedPrefs

    private var permissionsToRequest = arrayListOf<String>()
    private var permissionsRejected = arrayListOf<String>()
    private var permissions = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestWindowFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_main)

        mainLayout.setListener(this)

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
                id.trim().isBlank() -> {
                    inputId.error = getString(R.string.id_warn_error_text)
                    setLoading(false)
                }
                password.trim().isBlank() -> {
                    inputPassword.error =
                        getString(R.string.password_warn_error_text)
                    setLoading(false)
                }
                else -> {
                    akun.addListenerForSingleValueEvent(accountValueEventListener())
                }
            }
        }
        initPermissions()
    }

    private fun initPermissions() {
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissions.add(Manifest.permission.CAMERA)

        permissionsToRequest = findUnAskedPermissions(permissions)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size > 0) {
                requestPermissions(
                    permissionsToRequest.toTypedArray(),
                    TambahLaporanFragment.ALL_PERMISSIONS_RESULT
                )
            }
        }
    }

    private fun findUnAskedPermissions(permissions: ArrayList<String>): ArrayList<String> {
        var result = arrayListOf<String>()
        permissions.forEachIndexed { _, s ->
            if (!hasPermission(s)) {
                result.add(s)
            }
        }
        return result
    }

    private fun hasPermission(permission: String): Boolean {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
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
            TambahLaporanFragment.ALL_PERMISSIONS_RESULT -> {
                permissions.forEachIndexed { _, s ->
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
                                            TambahLaporanFragment.ALL_PERMISSIONS_RESULT
                                        )
                                    }
                                })
                            return
                        }
                    }
                } else findUnAskedPermissions(permissionsRejected)
            }
        }

    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok_text), okListener)
            .setNegativeButton(getString(R.string.cancel_text), null).create().show()
    }

    private fun accountValueEventListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                setLoading(false)
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val user = p0.child(inputId.text.toString())
                    if (user.exists()) {
                        val account = user.getValue<Akun>(Akun::class.java)
                        if (account != null)
                            if (account.password.toString() == inputPassword.text.toString()) {

                                when (account.access) {
                                    ID_OFFICER -> {
                                        HomeActivity.start(this@LoginActivity)
                                        preferences.saveInt(IS_LOGGED_IN, ID_OFFICER)
                                        preferences.saveString(
                                            USER_VALUE,
                                            Gson().toJson(account)
                                        )
                                    }
                                    ID_RT -> {
                                        HomeRTActivity.start(this@LoginActivity)
                                        preferences.saveInt(IS_LOGGED_IN, ID_RT)
                                        preferences.saveString(
                                            USER_VALUE,
                                            Gson().toJson(account)
                                        )
                                    }
                                    else -> {
                                        toastMe(getString(R.string.no_access_account_text))
                                    }
                                }
                            } else {
                                toastMe(getString(R.string.wrong_password_text))
                            }
                        else toastMe(getString(R.string.account_not_found_text))
                    } else toastMe(getString(R.string.account_not_found_text))
                }
                setLoading(false)
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

    override fun onSoftKeyboardShown(isShowing: Boolean) = isShowing.let {
        imageview.visibility(it.not())
        textHintLogin.visibility(it)
    }
}
