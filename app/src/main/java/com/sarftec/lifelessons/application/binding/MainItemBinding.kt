package com.sarftec.lifelessons.application.binding

import android.net.Uri
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.sarftec.lifelessons.BR
import com.sarftec.lifelessons.application.adapter.MainItemAdapter
import com.sarftec.lifelessons.application.adapter.UriContainer
import com.sarftec.lifelessons.application.file.bindable
import com.sarftec.lifelessons.application.file.vibrate
import com.sarftec.lifelessons.application.model.MainItem

class MainItemBinding(
    private val capsule: MainItemAdapter.Capsule,
    val item: MainItem
) : BaseObservable() {

    @get:Bindable
    var coilImage: UriContainer by bindable(UriContainer.Empty, BR.coilImage)

    fun init() {
        with(capsule.dependency.imageStore()) {
            loadImage(pictureImage(item.category))
        }
    }

    private fun loadImage(imageUri: Uri) {
        coilImage = UriContainer.UriImage(
            capsule.dependency.imageLoader(),
            imageUri,
            capsule.imagePlaceHolder
        )
    }

    fun onClick() {
        with(capsule) {
            dependency.context().vibrate()
           capsule.onClick(item)
        }
    }
}