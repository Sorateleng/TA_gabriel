package stta.gabriel.ta_gabriel.view.menu.rt.kirimlaporan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_kirim_laporan.*
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.model.ItemLaporan
import stta.gabriel.ta_gabriel.model.Lokasi
import java.text.SimpleDateFormat
import java.util.*

class KirimLaporanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kirim_laporan)

        btnkrmlprn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val formatInputDetail = "yyyy-MM-dd HH:mm:ss"
            val sdf = SimpleDateFormat(formatInputDetail)
            val newItem=ItemLaporan(
                foto1 = "dummy",
                foto2 = "dummy",
                pelapor = "dummy",
                status = 1,
                head = 999,
                lokasi = Lokasi(lat = 0,long = 0),
                id_user = 1,
                tanggal_laporan = sdf.format(calendar.time)


            )

            val dbReference = FirebaseDatabase.getInstance().reference

            dbReference
                .child("laporan")
                .child(newItem.head.toString())
                .setValue(newItem)
                .addOnSuccessListener { Toast.makeText(this, "success", Toast.LENGTH_SHORT).show() }
                .addOnFailureListener { Toast.makeText(this,"failed", Toast.LENGTH_SHORT).show() }
                .addOnCompleteListener { finish() }
        }
    }
}