package stta.gabriel.ta_gabriel.detaillaporan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_detail_laporan.*
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.menu.laporan.KEY_DATA_LAPORAN
import stta.gabriel.ta_gabriel.model.ItemLaporan
import stta.gabriel.ta_gabriel.util.loadSquareImageFromUrl

class DetailLaporanActivity : AppCompatActivity() {

    private val item by lazy { intent.getParcelableExtra(KEY_DATA_LAPORAN) as ItemLaporan }
    private lateinit var laporan: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_laporan)

        img_laporandarirt.loadSquareImageFromUrl(item.foto1)
        inputlokasi.setText("${item.lokasi.lat} - ${item.lokasi.long}")
        inputrt.setText("${item.pelapor}")

        laporan = FirebaseDatabase.getInstance().reference.child("laporan").child(item.head.toString())
        laporan.keepSynced(true)
        btn_proses.setOnClickListener{
            item.status=2
            laporan.setValue(item).addOnSuccessListener { Toast.makeText(this,"Berhasil diubah",Toast.LENGTH_SHORT).show() }
                .addOnCompleteListener { this.finish() }
        }



    }
}
