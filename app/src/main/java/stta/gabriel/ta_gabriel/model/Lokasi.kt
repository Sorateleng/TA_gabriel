package stta.gabriel.ta_gabriel.model

data class Lokasi(
    val lat: Int,
    val long: Int
){
    constructor():this(0,0)
}