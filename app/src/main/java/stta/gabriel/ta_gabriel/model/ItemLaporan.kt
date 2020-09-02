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
    val head: Long,
    val id_user: Int,
    val tanggal_laporan: String
) :Parcelable
{
    constructor() : this("", "", "", 0, Lokasi(0.0, 0.0), 0, 0, "")
}