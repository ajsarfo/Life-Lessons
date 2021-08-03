package com.sarftec.lifelessons.application.injection

import android.graphics.Bitmap
import android.util.LruCache
import com.sarftec.lifelessons.application.image.BitmapCache
import com.sarftec.lifelessons.application.image.BitmapCacheImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AbstractAppModule {

    @Singleton
    @Binds
    fun appBitmapCache(bitmapCache: BitmapCacheImpl) : BitmapCache
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun lruBitmapCache(): LruCache<String, Bitmap> {
        val cacheSize = Runtime.getRuntime().maxMemory() / 1024 / 8
        return object : LruCache<String, Bitmap>(cacheSize.toInt()) {
            override fun sizeOf(key: String?, value: Bitmap): Int {
                return value.byteCount / 1024
            }
        }
    }
}