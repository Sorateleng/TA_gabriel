package stta.gabriel.ta_gabriel.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemUlasan (
    val id_laporan : String,
    val id_user : String,
    val isi : String

) : Parcelable

{
    constructor ():this ("","","" )
}