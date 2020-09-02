package stta.gabriel.ta_gabriel.view.menu.officer.laporan


import android.content.Context
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
import stta.gabriel.ta_gabriel.util.default
import stta.gabriel.ta_gabriel.view.detaillaporan.DetailLaporanActivity
import stta.gabriel.ta_gabriel.view.detaillaporan.DetailLaporanActivity.Companion.startDetail
import stta.gabriel.ta_gabriel.view.menu.officer.HomeActivity

class LaporanFragment : Fragment(), LaporanAdapter.ItemAdapterCallback {
    private var stockList: MutableList<ItemLaporan> = mutableListOf()
    private lateinit var itemAdapter: LaporanAdapter
    private lateinit var laporan: DatabaseReference
    private lateinit var topActivity: HomeActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        topActivity = context as HomeActivity
    }

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
        getLaporanAll()
    }

    private fun getLaporanAll() {
        val listUndone = mutableListOf<ItemLaporan>()
        val listProgress = mutableListOf<ItemLaporan>()
        laporan.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listUndone.clear()
                listProgress.clear()
                stockList.clear()

                if (dataSnapshot.exists()) {
                    for (data in dataSnapshot.children) {
                        val item = data.getValue<ItemLaporan>(ItemLaporan::class.java)
//                        if (item?.id_user == topActivity.akun.head.default()) {
                        if (item?.status == ID_UNDONE) listUndone.add(item)
                        else if (item?.status == ID_PROGRESS) listProgress.add(item)
//                        }
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
        startActivity(startDetail(intent, item, item.status))
    }

    companion object {
        fun newInstance(): Fragment {
            return LaporanFragment()
        }
    }

}