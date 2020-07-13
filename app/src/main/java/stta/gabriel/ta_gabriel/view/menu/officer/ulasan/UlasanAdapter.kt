package stta.gabriel.ta_gabriel.view.menu.officer.ulasan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_ulasan.view.*
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.model.ItemUlasan


class UlasanAdapter(
    private val items: MutableList<ItemUlasan>,
    private val callback: ItemAdapterCallback
) : RecyclerView.Adapter<UlasanAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_ulasan,parent, false)
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


        fun bind(item: ItemUlasan) {
            itemView.apply {
                textViewItemUlasan.text = item.isi
                setOnClickListener { callback.itemClick(item) }
            }


        }


    }

    interface ItemAdapterCallback {
        fun itemClick(item: ItemUlasan)

    }
}