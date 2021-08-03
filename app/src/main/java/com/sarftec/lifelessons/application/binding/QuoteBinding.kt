package com.sarftec.lifelessons.application.binding

import com.sarftec.lifelessons.application.file.vibrate
import com.sarftec.lifelessons.application.Dependency
import com.sarftec.lifelessons.application.viewmodel.QuoteViewModel
import com.sarftec.lifelessons.data.database.entity.Quote

class QuoteBinding(
    dependency: Dependency,
    listItem: Quote,
    private val viewModel: QuoteViewModel
) : BaseBinding(dependency, listItem) {

   val message: String = listItem.message

    fun init() {
        viewModel.getImage()?.let {
            changeImage(it)
        }
    }

    override fun onFavorite() {
        super.onFavorite()
        viewModel.saveFavorite(quote.favorite)
    }

    fun onImageChange() {
        dependency.context().vibrate()
        dependency.imageStore().randomGradientImage().let {
            changeImage(it)
            viewModel.changeImage(it)
        }
    }
}