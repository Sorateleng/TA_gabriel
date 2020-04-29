package stta.gabriel.ta_gabriel.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemRiwayat(
    val riwayat1: String,
    val pelapor: String,
    val status: String,
    val lokasi: Lokasi,
    val head: Int

) : Parcelize
{
    constructor()this :( riwayat1 "", pelapor "", status 2 , lokasi (lat 0, long 0), head 0)
}

