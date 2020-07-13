package stta.gabriel.ta_gabriel.view.menu.officer.ulasan


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_laporan.*
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.model.ItemUlasan

/**
 * A simple [Fragment] subclass.
 */
class UlasanFragment : Fragment(), UlasanAdapter.ItemAdapterCallback {
    private var ulasanList: MutableList<ItemUlasan> = mutableListOf()
    private lateinit var itemAdapter: UlasanAdapter
    private lateinit var ulasan: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_laporan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemAdapter = UlasanAdapter(ulasanList, this)
        rv_laporan.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = itemAdapter
        }
        ulasan = FirebaseDatabase.getInstance().reference.child(ULASAN)
        ulasan.keepSynced(true)
        getulasanList()
    }

    private fun getulasanList() {
        val list = mutableListOf<ItemUlasan>()
        ulasan.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                ulasanList.clear()

                if (dataSnapshot.exists()) {
                    for (data in dataSnapshot.children) {
                        val item = data.getValue(ItemUlasan::class.java)
                        item?.let { list.add(it) }
                    }
                }
                ulasanList.addAll(list)
                itemAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun itemClick(item: ItemUlasan) {
        Toast.makeText(context, item.isi, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance(): Fragment {
            return UlasanFragment()
        }
    }

}

const val KEY_DATA_ULASAN = "KEY_DATA_ULASAN"
const val ULASAN = "ulasan"