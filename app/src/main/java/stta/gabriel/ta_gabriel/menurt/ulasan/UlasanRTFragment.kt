package stta.gabriel.ta_gabriel.menurt.ulasan

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import stta.gabriel.ta_gabriel.R

class UlasanRTFragment : Fragment() {
  override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ulasan_rt, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            UlasanRTFragment()
    }
}
