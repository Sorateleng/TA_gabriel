package stta.gabriel.ta_gabriel.view.menu.officer.laporan


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
import stta.gabriel.ta_gabriel.model.ItemLaporan
import stta.gabriel.ta_gabriel.util.ID_PROGRESS
import stta.gabriel.ta_gabriel.util.ID_UNDONE
import stta.gabriel.ta_gabriel.util.TABLE_LAPORAN
import stta.gabriel.ta_gabriel.view.detaillaporan.DetailLaporanActivity
import stta.gabriel.ta_gabriel.view.detaillaporan.DetailLaporanActivity.Companion.startDetail

class LaporanFragment : Fragment(), LaporanAdapter.ItemAdapterCallback {
    private var stockList: MutableList<ItemLaporan> = mutableListOf()
    private lateinit var itemAdapter: LaporanAdapter
    private lateinit var laporan: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_laporan, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemAdapter = LaporanAdapter(stockList, this)
        rv_laporan.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = itemAdapter
        }
        laporan = FirebaseDatabase.getInstance().reference.child(TABLE_LAPORAN)
        laporan.keepSynced(true)
        getStockList()
    }

    private fun getStockList() {
        val listUndone = mutableListOf<ItemLaporan>()
        val listProgress = mutableListOf<ItemLaporan>()
        laporan.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listUndone.clear()
                stockList.clear()

                if (dataSnapshot.exists()) {
                    for (data in dataSnapshot.children) {
                        val itemUndone = data.getValue<ItemLaporan>(ItemLaporan::class.java)

                        if (itemUndone?.status == ID_UNDONE) listUndone.add(itemUndone)
                        else if (itemUndone?.status == ID_PROGRESS) listProgress.add(itemUndone)
                    }
                }
                stockList.addAll(listUndone)
                stockList.addAll(listProgress)
                itemAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun itemClick(item: ItemLaporan) {
        val intent = Intent(context, DetailLaporanActivity::class.java)
        startActivity(startDetail(intent, item, false))
    }

    companion object {
        fun newInstance(): Fragment {
            return LaporanFragment()
        }
    }

}