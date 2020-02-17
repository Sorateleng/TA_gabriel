package stta.gabriel.ta_gabriel.menu


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import stta.gabriel.ta_gabriel.R

/**
 * A simple [Fragment] subclass.
 */
class LaporanFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_laporan, container, false)
    }

    companion object {
        fun newInstance(): Fragment {
            return LaporanFragment()
        }
    }

}
