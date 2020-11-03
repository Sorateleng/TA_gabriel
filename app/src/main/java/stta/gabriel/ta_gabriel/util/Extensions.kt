package stta.gabriel.ta_gabriel.util

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import stta.gabriel.ta_gabriel.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun ImageView.loadImageFromUrl(url: String, placeHolder: Int = R.drawable.ic_launcher_background) {
    Glide.with(context.applicationContext)
        .load(url)
        .placeholder(placeHolder)
        .into(this)
}

fun ImageView.loadImageFromBitmap(
    url: Bitmap,
    placeHolder: Int = R.drawable.ic_launcher_background
) {
    Glide.with(context.applicationContext)
        .load(url)
        .placeholder(placeHolder)
        .into(this)
}

fun ImageView.loadImageFromFile(url: File, placeHolder: Int = R.drawable.ic_launcher_background) {
    Glide.with(context.applicationContext)
        .load(url)
        .placeholder(placeHolder)
        .into(this)
}

fun ImageView.loadCircleImageFromUrl(
    url: String,
    placeHolder: Int = R.drawable.ic_launcher_background
) {
    Glide.with(context.applicationContext)
        .load(url)
        .placeholder(placeHolder)
        .apply(RequestOptions().circleCrop())
        .into(this)
}

fun ImageView.loadSquareImageFromUrl(
    url: String,
    placeHolder: Int = R.drawable.ic_launcher_background
) {
    Glide.with(context.applicationContext)
        .load(url)
        .placeholder(placeHolder)
        .apply(RequestOptions.bitmapTransform(RoundedCorners(12)))
        .into(this)
}

fun View.setVisible() {
    if (this.visibility != View.VISIBLE) this.visibility = View.VISIBLE
}

fun View.setGone() {
    if (this.visibility != View.GONE) this.visibility = View.GONE
}

fun View.setInvisible() {
    if (this.visibility != View.INVISIBLE) this.visibility = View.INVISIBLE
}

fun View.visibility(isShow: Boolean) {
    if (isShow) this.setVisible() else this.setGone()
}

fun Int?.default() = this ?: 0
fun String?.default() = this ?: ""
fun Boolean?.default() = this ?: false

fun getCurrentTime(): String {
    val calendar = Calendar.getInstance()
    val formatInputDetail = "yyyy-MM-dd HH:mm:ss"
    val sdf = SimpleDateFormat(formatInputDetail)
    return sdf.format(calendar.time)
}

fun URI_MAPS(lat: Double, long: Double, address: String): String {
    return "$MAPS_BY_LONGLAT$lat,$long($address)"
}