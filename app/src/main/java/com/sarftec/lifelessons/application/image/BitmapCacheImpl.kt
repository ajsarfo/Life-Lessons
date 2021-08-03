package com.sarftec.lifelessons.application.image

import android.graphics.Bitmap
import android.util.LruCache
import javax.inject.Inject

class BitmapCacheImpl @Inject constructor(
    private val lruCache: LruCache<String, Bitmap>
) : BitmapCache {

    override fun getBitmap(key: String): Bitmap? {
        return lruCache.get(key)
    }

    override fun saveBitmap(key: String, bitmap: Bitmap) {
        lruCache.put(key, bitmap)
    }
}