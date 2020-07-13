package stta.gabriel.ta_gabriel.view.menu.rt.tambahlaporan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import stta.gabriel.ta_gabriel.R

class TambahLaporanFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tambah_laporan, container, false)
    }

    companion object {
        fun newInstance() = TambahLaporanFragment()
    }
}
