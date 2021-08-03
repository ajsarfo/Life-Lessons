package com.sarftec.lifelessons.application.binding

import com.sarftec.lifelessons.application.file.vibrate
import com.sarftec.lifelessons.application.adapter.Capsule
import com.sarftec.lifelessons.application.enums.Destination
import com.sarftec.lifelessons.data.database.entity.Quote

class ListItemBinding(
    quote: Quote,
    private val position: Int,
    private val capsule: Capsule
) : BaseBinding(capsule.dependency, quote) {

    fun init() {
        changeImage(capsule.imageCache.get(position))
    }

    override fun onFavorite() {
        super.onFavorite()
        capsule.viewModel.saveItem(quote)
    }

    fun onClick() {
        with(capsule) {
            dependency.context().vibrate()
            viewModel.persistCallbacks(
                { uri ->
                    imageCache.replace(position, uri)
                    changeImage(uri)
                },
                { isFavorite ->
                    quote.favorite = isFavorite
                    favoriteIcon = getFavoriteDrawable()
                    viewModel.saveItem(quote)
                }
            )
            viewModel.persistItem(quote, imageCache.get(position))
            dependency.navigationListener().navigate(Destination.QUOTE)
        }
    }
}