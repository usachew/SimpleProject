package com.usacheow.coreuikit.utils.ext

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.load(url: String?) {
    url ?: return
    Glide.with(this).load(url).into(this)
}