package com.sarftec.lifelessons.application.binding

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.sarftec.lifelessons.BR
import com.sarftec.lifelessons.application.file.moreApps
import com.sarftec.lifelessons.application.file.rateApp
import com.sarftec.lifelessons.application.file.share
import com.sarftec.lifelessons.application.file.vibrate
import com.sarftec.lifelessons.R
import com.sarftec.lifelessons.application.Dependency
import com.sarftec.lifelessons.application.file.bindable
import com.sarftec.lifelessons.application.image.ImageHolder
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AboutDialogBinding(
    private val dependency: Dependency,
) : BaseObservable() {

    @get:Bindable
    var appImage : ImageHolder by bindable(ImageHolder.Empty, BR.appImage)

    fun init() {
        with(dependency) {
            coroutineScope().launch {
                loadImageAsync(imageStore().appImage()).collect { bitmap ->
                    bitmap?.let {
                        appImage = ImageHolder.ImageBitmap(it)
                        throw CancellationException()
                    }
                }
            }
        }
    }

    fun rate() {
        with(dependency.context()) {
            vibrate()
            rateApp()
        }
    }

    fun share() {
        with(dependency.context()) {
            vibrate()
            share(
                "${getString(R.string.app_share_message)}\n\nhttps://play.google.com/store/apps/details?id=$packageName}",
                "Share"
            )
        }
    }

    fun moreApps() {
        with(dependency.context()) {
            vibrate()
            moreApps()
        }
    }
}