package com.sarftec.lifelessons.application.adapter

import android.net.Uri
import com.sarftec.lifelessons.application.Dependency

class ImageCache(private val dependency: Dependency) {

    private val imageMap = hashMapOf<Int, Uri>()

    fun get(quoteNumber: Int): Uri {
        return imageMap.getOrPut(quoteNumber) {
            dependency.imageStore().randomGradientImage()
        }
    }

    fun replace(quoteNumber: Int, uri: Uri) {
        imageMap[quoteNumber] = uri
    }
}