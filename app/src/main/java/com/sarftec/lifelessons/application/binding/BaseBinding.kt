package com.sarftec.lifelessons.application.binding

import android.net.Uri
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.sarftec.lifelessons.application.file.copy
import com.sarftec.lifelessons.application.file.share
import com.sarftec.lifelessons.application.file.toast
import com.sarftec.lifelessons.application.file.vibrate
import com.sarftec.lifelessons.BR
import com.sarftec.lifelessons.R
import com.sarftec.lifelessons.application.Dependency
import com.sarftec.lifelessons.application.adapter.UriContainer
import com.sarftec.lifelessons.application.file.bindable
import com.sarftec.lifelessons.application.image.ImageHolder
import com.sarftec.lifelessons.data.database.entity.Quote

abstract class BaseBinding(
    protected val dependency: Dependency,
    val quote: Quote
) : BaseObservable() {

    @get:Bindable
    var favoriteIcon: ImageHolder by bindable(getFavoriteDrawable(), BR.favoriteIcon)

    @get:Bindable
    var coilImage: UriContainer by bindable(UriContainer.Empty, BR.coilImage)

    fun onCopy() {
        with(dependency.context()) {
            vibrate()
            toast("Copied to clipboard")
            copy(quote.message, "label")
        }
    }

    fun onShare() {
        with(dependency.context()) {
            vibrate()
            share(quote.message, "Copy")
        }
    }

    open fun onFavorite() {
        dependency.context().vibrate()
        with(quote) {
            favorite = !favorite
        }
        favoriteIcon = getFavoriteDrawable()
    }

    protected fun changeImage(uri: Uri) {
        coilImage = UriContainer.UriImage(dependency.imageLoader(), uri, allowHardware = false)
    }

    protected fun getFavoriteDrawable() : ImageHolder.ImageDrawable {
        return ImageHolder.ImageDrawable(
            if(quote.favorite) R.drawable.ic_star_filled else R.drawable.ic_star_unfilled
        )
    }
}