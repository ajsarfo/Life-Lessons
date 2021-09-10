package com.sarftec.lifelessons.application

import android.content.Context
import androidx.lifecycle.lifecycleScope
import com.sarftec.lifelessons.application.activity.BaseActivity
import com.sarftec.lifelessons.application.image.BitmapImageLoader
import com.sarftec.lifelessons.application.image.ImageStore
import kotlinx.coroutines.CoroutineScope

class Dependency(private val activity: BaseActivity) {

    fun coroutineScope() : CoroutineScope = activity.lifecycleScope

    fun context() : Context = activity

    fun imageStore() : ImageStore = activity.imageStore

    fun imageLoader() : BitmapImageLoader = activity.imageLoader
}