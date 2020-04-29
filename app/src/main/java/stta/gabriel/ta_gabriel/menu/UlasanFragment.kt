package stta.gabriel.ta_gabriel.menu.riwayat


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_laporan.*
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.detaillaporan.DetailLaporanActivity
import stta.gabriel.ta_gabriel.menu.UlasanAdapter
import stta.gabriel.ta_gabriel.model.ItemLaporan
import stta.gabriel.ta_gabriel.model.ItemUlasan

/**
 * A simple [Fragment] subclass.
 */
class UlasanFragment: Fragment(),UlasanAdapter.ItemAdapterCallback {
    private var stockList: MutableList<ItemUlasan> = mutableListOf()
    private lateinit var itemAdapter: UlasanAdapter
    private lateinit var laporan: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ulasan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemAdapter = UlasanAdapter(stockList, this)
        rv_laporan.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = itemAdapter
        }
        laporan = FirebaseDatabase.getInstance().reference.child("ulasan")
        laporan.keepSynced(true)
        getStockList()
    }

    private fun getStockList() {
        val list = mutableListOf<ItemUlasan>()
        laporan.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                stockList.clear()

                if (dataSnapshot.exists()) {
                    for (data in dataSnapshot.children) {
                        val item = data.getValue<ItemUlasan>(ItemLaporan::class.java)
                        if (item?.status == 3)
                            list.add(item)
                    }
                }
                stockList.addAll(list)
                itemAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun itemClick(item: ItemUlasan) {
        val intent = Intent (context,DetailLaporanActivity::class.java)
        intent.putExtra(KEY_DATA_LAPORAN , item)
        startActivity(intent)
    }

    companion object {
        fun newInstance(): Fragment {
            return RiwayatFragment()
        }
    }

}
const val KEY_DATA_LAPORAN = "KEY_DATA_LAPORAN "