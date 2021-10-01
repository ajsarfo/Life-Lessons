package com.sarftec.lifelessons.application.file

import android.widget.ImageView
import androidx.databinding.BaseObservable
import androidx.databinding.BindingAdapter
import coil.load
import com.google.android.material.card.MaterialCardView
import com.sarftec.lifelessons.application.adapter.Capsule
import com.sarftec.lifelessons.application.adapter.UriContainer
import com.sarftec.lifelessons.application.image.ImageHolder
import kotlin.reflect.KProperty

class Bindable<T : Any>(private var value: T, private val tag: Int) {
    operator fun <U : BaseObservable> getValue(ref: U, property: KProperty<*>): T = value
    operator fun <U : BaseObservable> setValue(ref: U, property: KProperty<*>, newValue: T) {
        value = newValue
        ref.notifyPropertyChanged(tag)
    }
}


fun <T : Any> bindable(value: T, tag: Int): Bindable<T> = Bindable(value, tag)

@BindingAdapter("coilImage")
fun loadImage(imageView: ImageView, container: UriContainer) {
    when (container) {
        is UriContainer.UriImage -> {
            imageView.load(
                container.imageUri,
                container.imageLoader.imageLoader
            ) {
                allowHardware(container.allowHardware)
                placeholder(container.placeHolder)
            }
        }
        else -> {

        }
    }
}

@BindingAdapter("image")
fun changeImage(imageView: ImageView, imageHolder: ImageHolder?) {
    imageHolder?.let {
        when (it) {
            is ImageHolder.ImageBitmap -> imageView.setImageBitmap(it.image)
            is ImageHolder.ImageDrawable -> imageView.setImageResource(it.icon)
            else -> {
            }
        }
    }
}

@BindingAdapter("color")
fun changeBackgroundColor(cardView: MaterialCardView, color: Int) {
    cardView.setCardBackgroundColor(color)
}