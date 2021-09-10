package com.sarftec.lifelessons.application.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.sarftec.lifelessons.BR
import com.sarftec.lifelessons.R
import com.sarftec.lifelessons.application.file.bindable
import com.sarftec.lifelessons.application.image.ImageHolder
import com.sarftec.lifelessons.data.database.entity.Quote

class QuoteBottomPanel(
    val listener: Listener
) : BaseObservable() {

    @get:Bindable
    var favoriteIcon: ImageHolder by bindable(ImageHolder.Empty, BR.favoriteIcon)

    fun setQuote(quote: Quote) {
        favoriteIcon = ImageHolder.ImageDrawable(
            if (quote.favorite) R.drawable.ic_star_filled else R.drawable.ic_star_unfilled
        )
    }

    interface Listener {
        fun randomBackground()
        fun launchTextPanel()
        fun chooseBackground()
        fun launchImagePreview()
        fun copy()
        fun changeFavorite()
    }
}