package stta.gabriel.ta_gabriel.model

data class ItemLaporan(
    val foto1: String,
    val foto2: String,
    val pelapor: String,
    val status: Int,
    val lokasi: Lokasi
){
    constructor():this("", "", "", 0, Lokasi(0, 0))
}