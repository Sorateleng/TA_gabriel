package stta.gabriel.ta_gabriel.view.menu.officer.riwayat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_riwayat.view.*
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.model.ItemLaporan

class RiwayatAdapter(
    private val items: MutableList<ItemLaporan>,
    private val callback: ItemAdapterCallback
) : RecyclerView.Adapter<RiwayatAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat,parent, false)
        return Holder(itemView = view)
    }

    override fun getItemCount() = items.size
    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = position

    override fun onBindViewHolder(p0: Holder, p1: Int) {
        val item = items[p1]
        p0.bind(item)
    }


    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(item: ItemLaporan) {
            itemView.apply {
                textViewItemRiwayat.text = item.pelapor
                setOnClickListener { callback.itemClick(item) }
            }


        }


    }

    interface ItemAdapterCallback {
        fun itemClick(item: ItemLaporan)

    }
}