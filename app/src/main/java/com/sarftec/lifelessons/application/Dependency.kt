package com.sarftec.lifelessons.application

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.lifecycleScope
import com.sarftec.lifelessons.application.activity.BaseActivity
import com.sarftec.lifelessons.application.listener.NavigationListener
import com.sarftec.lifelessons.application.image.BitmapImageLoader
import com.sarftec.lifelessons.application.image.ImageStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

class Dependency(private val activity: BaseActivity) {

    fun loadImageAsync(uri: Uri) : StateFlow<Bitmap?> {
        return activity.imageLoader.loadImageAsync(uri)
    }

    fun navigationListener() : NavigationListener = activity

    fun coroutineScope() : CoroutineScope = activity.lifecycleScope

    fun context() : Context = activity

    fun imageStore() : ImageStore = activity.imageStore

    fun imageLoader() : BitmapImageLoader = activity.imageLoader
}