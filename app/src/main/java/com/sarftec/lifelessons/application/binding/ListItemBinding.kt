package com.sarftec.lifelessons.application.binding

import com.sarftec.lifelessons.application.adapter.Capsule
import com.sarftec.lifelessons.application.file.toast
import com.sarftec.lifelessons.data.database.entity.Quote

class ListItemBinding(
    quote: Quote,
    private val position: Int,
    private val capsule: Capsule,
    val onCaptureImage: () -> Unit
) : BaseBinding(capsule.dependency, quote) {

    fun init() {
        changeImage(capsule.imageCache.get(position))
    }

    override fun onFavorite() {
        super.onFavorite()
        capsule.apply {
            dependency.context().toast(
                if (quote.favorite)
                    "Added to Favorites"
                else "Removed from favorites"
            )
            capsule.viewModel.saveItem(quote)
        }
    }

    fun switchImage() {
        capsule.imageCache.apply {
            val randImage = random()
            replace(position, randImage)
            changeImage(randImage)
        }
    }

    fun onClick() {
        capsule.onClick(quote, capsule.imageCache.get(position))
    }
}