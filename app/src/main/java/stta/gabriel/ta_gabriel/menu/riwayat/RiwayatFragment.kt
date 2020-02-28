package stta.gabriel.ta_gabriel.menu.riwayat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_laporan.*

import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.model.ItemRiwayat

/**
 * A simple [Fragment] subclass.
 */
class RiwayatFragment : Fragment(),RiwayatAdapter.ItemAdapterCallback {
    private var stockList: MutableList<ItemRiwayat> = mutableListOf()
    private lateinit var itemAdapter: RiwayatAdapter
    private lateinit var stock: DatabaseReference
    lateinit var dbReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_riwayat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemAdapter = RiwayatAdapter(stockList, this)
        rv_laporan.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = itemAdapter
        }
        stock = dbReference.child("Riwayat")
        stock.keepSynced(true)
        getStockList()
    }

    private fun getStockList() {
        val list = mutableListOf<ItemRiwayat>()
        stock.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                stockList.clear()

                if (dataSnapshot.exists()) {
                    for (data in dataSnapshot.children) {
                        val item = data.getValue(ItemRiwayat::class.java)!!
                            list.add(item)
                    }
                }
                stockList.addAll(list)
                itemAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun itemClick(item: ItemRiwayat) {
        Log.e("tot", item.toString())
    }

    companion object {
        fun newInstance(): Fragment {
            return RiwayatFragment()
        }
    }

}
