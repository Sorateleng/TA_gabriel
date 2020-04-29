package stta.gabriel.ta_gabriel.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemRiwayat(
    val riwayat: String,
    val pelapor: String,
    val status: String,
    val lokasi: Lokasi,
    val head: Int

) : Parcelable
{
    constructor():this ( "","","",Lokasi(0, 0),0)
}


