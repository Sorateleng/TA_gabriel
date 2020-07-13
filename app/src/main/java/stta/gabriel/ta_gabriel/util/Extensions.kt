package stta.gabriel.ta_gabriel.util

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import stta.gabriel.ta_gabriel.R

fun ImageView.loadImageFromUrl(url: String, placeHolder: Int = R.drawable.ic_launcher_background) {
    Glide.with(context.applicationContext)
        .load(url)
        .placeholder(placeHolder)
        .into(this)
}

fun ImageView.loadImageFromBitmap(url: Bitmap, placeHolder: Int = R.drawable.ic_launcher_background) {
    Glide.with(context.applicationContext)
        .load(url)
        .placeholder(placeHolder)
        .into(this)
}

fun ImageView.loadCircleImageFromUrl(url: String, placeHolder: Int = R.drawable.ic_launcher_background) {
    Glide.with(context.applicationContext)
        .load(url)
        .placeholder(placeHolder)
        .apply(RequestOptions().circleCrop())
        .into(this)
}

fun ImageView.loadSquareImageFromUrl(url: String, placeHolder: Int = R.drawable.ic_launcher_background) {
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