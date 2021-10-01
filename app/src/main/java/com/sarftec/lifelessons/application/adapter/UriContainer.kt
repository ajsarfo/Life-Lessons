package com.sarftec.lifelessons.application.adapter

import android.graphics.drawable.Drawable
import android.net.Uri
import com.sarftec.lifelessons.R
import com.sarftec.lifelessons.application.image.BitmapImageLoader

sealed class UriContainer {
    class UriImage(
        val imageLoader: BitmapImageLoader,
        val imageUri: Uri,
        val placeHolder: Drawable? = null,
        var allowHardware: Boolean = true
    ) : UriContainer()

    object Empty : UriContainer()
}