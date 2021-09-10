package com.sarftec.lifelessons.application.binding

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.sarftec.lifelessons.BR
import com.sarftec.lifelessons.application.Dependency
import com.sarftec.lifelessons.application.adapter.UriContainer
import com.sarftec.lifelessons.application.file.bindable
import com.sarftec.lifelessons.application.image.ImageHolder
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainToolbarBinding(
    private val dependency: Dependency,
    private val onFavorite: () -> Unit,
    private val onInfo: () -> Unit
) : BaseObservable() {

    @get:Bindable
    var coilImage: UriContainer by bindable(UriContainer.Empty, BR.coilImage)

    fun init() {
        dependency.apply {
            coilImage = UriContainer.UriImage(imageLoader(), imageStore().appImage())
        }
    }

    fun onFavorite() = onFavorite.invoke()

    fun onInfo() = onInfo.invoke()
}