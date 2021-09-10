package com.sarftec.lifelessons.application.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import coil.ImageLoader
import coil.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/*
This class in pretty much not doing anything.
For the sake of not breaking any code through out the application,
we are keeping it!
 */
@Singleton
class BitmapImageLoader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    //Note this is made public and referenced for image loading purpose
    val imageLoader = ImageLoader(context)
}