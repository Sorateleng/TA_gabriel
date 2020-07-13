package stta.gabriel.ta_gabriel.view.detaillaporan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_detail_laporan.*
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.model.ItemLaporan
import stta.gabriel.ta_gabriel.util.TABLE_LAPORAN
import stta.gabriel.ta_gabriel.util.loadSquareImageFromUrl

class DetailLaporanActivity : AppCompatActivity() {

    private val item by lazy { intent.getParcelableExtra(KEY_DATA_LAPORAN) as ItemLaporan }
    private val isDone by lazy { intent.getBooleanExtra(IS_DONE, true) }
    private lateinit var laporan: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_laporan)

        setData(
            item.foto1,
            item.foto2,
            "${item.lokasi.lat} - ${item.lokasi.long}",
            item.pelapor
        )

        if (isDone) {
            btn_proses.text = "AMBIL FOTO SELESAI"
        }

        laporan = FirebaseDatabase
            .getInstance()
            .reference
            .child(TABLE_LAPORAN)
            .child(item.head.toString())

        Log.d("itemLaporan", item.toString())
        laporan.keepSynced(true)
    }

    private fun setData(foto1: String, foto2: String, lokasi: String, pelapor: String) {
        img_laporan1.loadSquareImageFromUrl(foto1)
        img_laporan2.loadSquareImageFromUrl(foto2)
        inputlokasi.setText(lokasi)
        inputrt.setText(pelapor)
        btn_proses.setOnClickListener {
            item.status = if (isDone) 3 else 2
            laporan.setValue(item).addOnSuccessListener {
                Toast.makeText(this, "Berhasil diubah", Toast.LENGTH_SHORT).show()
            }
                .addOnCompleteListener { this.finish() }
        }
    }

    companion object {
        const val KEY_DATA_LAPORAN = "KEY_DATA_LAPORAN"
        const val IS_DONE = "IS_DONE"
        fun startDetail(intent: Intent, itemLaporan: ItemLaporan, isDone: Boolean): Intent {
            intent.putExtra(KEY_DATA_LAPORAN, itemLaporan)
            intent.putExtra(IS_DONE, isDone)
            return intent
        }
    }
}
