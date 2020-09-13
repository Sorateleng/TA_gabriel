package stta.gabriel.ta_gabriel.view.menu.rt.ulasan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_ulasan_rt.*
import stta.gabriel.ta_gabriel.R
import stta.gabriel.ta_gabriel.model.Akun
import stta.gabriel.ta_gabriel.model.ItemUlasan
import stta.gabriel.ta_gabriel.util.SharedPrefs
import stta.gabriel.ta_gabriel.util.USER_VALUE
import stta.gabriel.ta_gabriel.util.default
import stta.gabriel.ta_gabriel.view.menu.rt.HomeRTActivity
import stta.gabriel.ta_gabriel.view.menu.rt.RtCallback

class UlasanRTFragment : Fragment() {
    private lateinit var callback: RtCallback
  override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ulasan_rt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_inputulasan.setOnClickListener {
            val text= inputulasan.text
            if (text.isEmpty()){
                Toast.makeText(context, "Ulasan Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            } else {
                val head = System.currentTimeMillis()
                val prefs = SharedPrefs(requireContext())
                val akun = Gson().fromJson(prefs.getString(USER_VALUE), Akun::class.java)
                val ulasan= ItemUlasan (
                    id_laporan = head,
                    id_user = akun.head.default(),
                    isi = text.toString()
                )
                val dbReference = FirebaseDatabase.getInstance().reference
                dbReference
                    .child("ulasan")
                    .child(ulasan.id_laporan.toString())
                    .setValue(ulasan)
                    .addOnSuccessListener {
                        inputulasan.setText("")
                        callback.toHomeListener()
                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show() }
                    .addOnFailureListener { Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show() }
                    }

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback=context as HomeRTActivity
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            UlasanRTFragment()
    }
}
