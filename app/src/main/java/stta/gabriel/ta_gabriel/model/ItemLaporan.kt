package stta.gabriel.ta_gabriel.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemLaporan(
    val foto1: String,
    var foto2: String,
    val pelapor: String,
    var status: Int,
    val lokasi: Lokasi,
    val head: Int,
    val id_user: Int
) :Parcelable
{
    constructor() : this("", "", "", 0, Lokasi(0, 0), 0, 0)
}