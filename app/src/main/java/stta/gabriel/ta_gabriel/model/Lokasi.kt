package stta.gabriel.ta_gabriel.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Lokasi(
    val lat: Int,
    val long: Int
):Parcelable{
    constructor():this(0,0)
}