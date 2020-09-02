package stta.gabriel.ta_gabriel.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Lokasi(
    val lat: Double,
    val long: Double
):Parcelable{
    constructor() : this(0.0, 0.0)
}