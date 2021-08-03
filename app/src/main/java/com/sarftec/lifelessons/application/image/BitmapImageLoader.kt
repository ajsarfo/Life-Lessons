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

@Singleton
class BitmapImageLoader @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val imageLoader = ImageLoader(context)
    private val imageLoadingScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


    fun loadImageAsync(uri: Uri): StateFlow<Bitmap?> {
        val stateFlow = MutableStateFlow<Bitmap?>(null)
        imageLoadingScope.launch {
            coilLoadImage(uri, stateFlow)
        }
        return stateFlow
    }

    private suspend fun coilLoadImage(uri: Uri, stateFlow: MutableStateFlow<Bitmap?>? = null) {
        try {
            val bitmap = imageLoader.execute(
                ImageRequest.Builder(context)
                    .data(uri)
                    .build()
            ).drawable.let { (it as BitmapDrawable).bitmap }
            stateFlow?.value = bitmap
        } catch (e: Exception) {
            //Log.v("TAG", "Cannot load $uri \nreason: $e")
        }
    }


    fun destroy() {
        imageLoadingScope.coroutineContext.cancelChildren()
    }


    /*
      @SuppressLint("CheckResult")
      private fun glideLoadImage(uri: Uri, stateFlow: MutableStateFlow<Bitmap?>? = null) {
          Glide.with(context)
              .asBitmap()
              .load(uri)
              .into(
                  object : CustomTarget<Bitmap>() {
                      override fun onResourceReady(
                          resource: Bitmap,
                          transition: Transition<in Bitmap>?
                      ) {
                          stateFlow?.value = resource
                          //  bitmapCache.saveBitmap(uri.toString(), resource)
                      }

                      override fun onLoadCleared(placeholder: Drawable?) {}
                  }
              )
      }
     */

    suspend fun preloadImages(uris: List<Uri>) {
       /*
        uris.forEach {
            coilLoadImage(it)
        }
        */
    }
}