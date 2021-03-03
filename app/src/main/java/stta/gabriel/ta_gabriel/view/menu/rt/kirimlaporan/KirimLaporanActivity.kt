package stta.gabriel.ta_gabriel.view.menu.rt.kirimlaporan

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_kirim_laporan.*
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.model.Akun
import stta.gabriel.ta_gabriel.model.ItemLaporan
import stta.gabriel.ta_gabriel.model.Lokasi
import stta.gabriel.ta_gabriel.util.*
import stta.gabriel.ta_gabriel.view.menu.rt.tambahlaporan.TambahLaporanFragment.Companion.IMAGE_EXTRA
import stta.gabriel.ta_gabriel.view.menu.rt.tambahlaporan.TambahLaporanFragment.Companion.LAT_EXTRA
import stta.gabriel.ta_gabriel.view.menu.rt.tambahlaporan.TambahLaporanFragment.Companion.LONG_EXTRA
import java.text.SimpleDateFormat
import java.util.*

class KirimLaporanActivity : AppCompatActivity() {
    private val img by lazy { intent.getStringExtra(IMAGE_EXTRA) }
    private val long by lazy { intent.getDoubleExtra(LONG_EXTRA, 0.0) }
    private val lat by lazy { intent.getDoubleExtra(LAT_EXTRA, 0.0) }

    private lateinit var prefs: SharedPrefs
    private lateinit var akun: Akun

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kirim_laporan)

        prefs = SharedPrefs(this)
        akun = Gson().fromJson(prefs.getString(USER_VALUE), Akun::class.java)

        imgkrmlprn.loadImageFromUrl(img)

        btnkrmlprn.setOnClickListener {
            val head = System.currentTimeMillis()
            val dbReference = FirebaseDatabase.getInstance().reference
            val newItem = ItemLaporan(
                foto1 = img,
                foto2 = "",
                pelapor = "Laporan dari RT ${akun.head}",
                status = 1,
                head = head,
                lokasi = Lokasi(lat = lat, long = long),
                id_user = akun.head.default(),
                tanggal_laporan = getCurrentTime(),
                tanggal_proses = "",
                tanggal_selesai = ""


            )
            dbReference
                .child("laporan")
                .child(newItem.head.toString())
                .setValue(newItem)
                .addOnSuccessListener { Toast.makeText(this, "success", Toast.LENGTH_SHORT).show() }
                .addOnFailureListener { Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show() }
                .addOnCompleteListener { finish() }
        }
    }

    override fun onBackPressed() {
        showDialog(
            cancelable = false,
            cancelableTouchOutside = true
        ) {
            setTitle("Apakah Anda yakin untuk membatalkannya?")
            positiveButton("Iya") {
                UploadImage().deleteImage(img)
                super.onBackPressed()
            }
            negativeButton("Tidak") {


            }
        }
    }
}