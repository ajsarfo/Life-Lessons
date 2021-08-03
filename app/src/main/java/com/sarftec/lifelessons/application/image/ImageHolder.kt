package com.sarftec.lifelessons.application.image

import android.graphics.Bitmap

sealed class ImageHolder {
    object Empty : ImageHolder()
    class ImageBitmap(val image: Bitmap) : ImageHolder()
    class ImageDrawable(val icon: Int) : ImageHolder()
}