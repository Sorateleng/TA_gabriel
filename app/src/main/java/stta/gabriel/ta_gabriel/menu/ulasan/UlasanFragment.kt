package stta.gabriel.ta_gabriel.menu.laporan


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
import stta.gabriel.ta_gabriel.model.ItemLaporan

/**
 * A simple [Fragment] subclass.
 */
class UlasanFragment: Fragment(), LaporanAdapter.ItemAdapterCallback {
    private var stockList: MutableList<ItemLaporan> = mutableListOf()
    private lateinit var itemAdapter: LaporanAdapter
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
        itemAdapter = LaporanAdapter(stockList, this)
        rv_laporan.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = itemAdapter
        }
        laporan = FirebaseDatabase.getInstance().reference.child("laporan")
        laporan.keepSynced(true)
        getStockList()
    }

    private fun getStockList() {
        val list = mutableListOf<ItemLaporan>()
        laporan.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                stockList.clear()

                if (dataSnapshot.exists()) {
                    for (data in dataSnapshot.children) {
                        val item = data.getValue<ItemLaporan>(ItemLaporan::class.java)
                        if (item?.status == 3)
                            list.add(item)
                    }
                }
                stockList.addAll(list)
                itemAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun itemClick(item: ItemLaporan) {
        val intent = Intent (context,DetailLaporanActivity::class.java)
        intent.putExtra(KEY_DATA_LAPORAN , item)
        startActivity(intent)
    }

    companion object {
        fun newInstance(): Fragment {
            return LaporanFragment()
        }
    }

}
const val KEY_DATA_LAPORAN = "KEY_DATA_LAPORAN "