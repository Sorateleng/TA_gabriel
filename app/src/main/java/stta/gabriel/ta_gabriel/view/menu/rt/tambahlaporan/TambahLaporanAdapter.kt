package stta.gabriel.ta_gabriel.view.menu.rt.tambahlaporan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_laporan.view.*
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.model.ItemLaporan
import stta.gabriel.ta_gabriel.util.ID_PROGRESS
import stta.gabriel.ta_gabriel.util.ID_UNDONE

class TambahLaporanAdapter(
    private val items: MutableList<ItemLaporan>,
    private val callback: TambahLaporanFragment
) : RecyclerView.Adapter<TambahLaporanAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_laporan, parent, false)
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
                itemTitle.text = item.pelapor
                itemStatus.text = when (item.status) {
                    ID_UNDONE -> "Belum dikerjakan"
                    ID_PROGRESS -> "Sedang Dikerjakan"
                    else -> {
                        setOnClickListener { callback.onClick(item) }
                        "Selesai Dikerjakan"
                    }
                }
            }
        }
    }

    interface ItemAdapterCallback {
        fun itemClick(item: ItemLaporan)

    }



}
