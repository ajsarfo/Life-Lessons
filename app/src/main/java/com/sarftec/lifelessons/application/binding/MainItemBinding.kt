package com.sarftec.lifelessons.application.binding

import android.net.Uri
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.sarftec.lifelessons.application.file.vibrate
import com.sarftec.lifelessons.BR
import com.sarftec.lifelessons.R
import com.sarftec.lifelessons.application.adapter.MainItemAdapter
import com.sarftec.lifelessons.application.enums.Destination
import com.sarftec.lifelessons.application.file.bindable
import com.sarftec.lifelessons.application.image.ImageHolder
import com.sarftec.lifelessons.application.model.MainItem
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class MainItemBinding(
    private val capsule: MainItemAdapter.Capsule,
    val item: MainItem
) : BaseObservable() {

    @get:Bindable
    var image: ImageHolder by bindable(ImageHolder.ImageDrawable(R.drawable.loading), BR.image)

    fun init() {
        with(capsule.dependency.imageStore()) {
            loadImage(pictureImage(item.category))
        }
    }

    private fun loadImage(imageUri: Uri) {
        with(capsule.dependency) {
            coroutineScope().launch {
                loadImageAsync(imageUri).collect { bitmap ->
                    bitmap?.let {
                        image = ImageHolder.ImageBitmap(it)
                        throw CancellationException()
                    }
                }
            }
        }
    }

    fun onClick() {
        with(capsule) {
            viewModel.persistItem(item)
            dependency.context().vibrate()
            dependency.navigationListener().navigate(Destination.LIST)
        }
    }
}