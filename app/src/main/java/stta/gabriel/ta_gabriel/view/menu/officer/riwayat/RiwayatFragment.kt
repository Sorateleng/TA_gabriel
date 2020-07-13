package stta.gabriel.ta_gabriel.view.menu.officer.riwayat


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_laporan.*
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.view.detaillaporan.DetailLaporanActivity
import stta.gabriel.ta_gabriel.view.detaillaporan.DetailLaporanActivity.Companion.startDetail
import stta.gabriel.ta_gabriel.model.ItemLaporan
import stta.gabriel.ta_gabriel.model.ItemRiwayat
import stta.gabriel.ta_gabriel.util.TABLE_LAPORAN
import stta.gabriel.ta_gabriel.util.TABLE_USER

/**
 * A simple [Fragment] subclass.
 */
class RiwayatFragment : Fragment(), RiwayatAdapter.ItemAdapterCallback {
    private var stockList: MutableList<ItemRiwayat> = mutableListOf()
    private lateinit var itemAdapter: RiwayatAdapter
    private lateinit var laporan: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_laporan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemAdapter = RiwayatAdapter(stockList, this)
        rv_laporan.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = itemAdapter
        }
        laporan = FirebaseDatabase.getInstance().reference.child(TABLE_LAPORAN)
        laporan.keepSynced(true)
        getStockList()
    }

    private fun getStockList() {
        val list = mutableListOf<ItemRiwayat>()
        laporan.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                stockList.clear()

                if (dataSnapshot.exists()) {
                    for (data in dataSnapshot.children) {
                        val item = data.getValue(ItemRiwayat::class.java)
                        if (item?.status == 2)
                            list.add(item)
                    }
                }
                stockList.addAll(list)
                itemAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun itemClick(item: ItemRiwayat) {
        val intent = Intent(context, DetailLaporanActivity::class.java)
        startDetail(
            intent, ItemLaporan(
                item.foto1,
                item.foto2,
                item.pelapor,
                item.status,
                item.lokasi,
                item.head
            ), true
        ).let { startActivity(it) }
    }

    companion object {
        fun newInstance(): Fragment {
            return RiwayatFragment()
        }
    }

}