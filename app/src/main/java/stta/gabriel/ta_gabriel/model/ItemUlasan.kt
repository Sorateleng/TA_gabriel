package stta.gabriel.ta_gabriel.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemUlasan (
    val id_laporan : Long,
    val id_user : Int,
    val isi : String

) : Parcelable

{
    constructor ():this (0,0,"" )
}