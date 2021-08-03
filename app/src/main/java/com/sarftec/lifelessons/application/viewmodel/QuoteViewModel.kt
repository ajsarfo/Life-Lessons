package com.sarftec.lifelessons.application.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.sarftec.lifelessons.application.Container
import com.sarftec.lifelessons.data.database.entity.Quote
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val container: Container
) : ViewModel() {

    val listItem: Quote? = container.selectedQuote

    fun getImage() : Uri? = container.selectedImageUri

    fun saveFavorite(isFavorite: Boolean) {
        container.onFavoriteChanged?.invoke(isFavorite)
    }

    fun changeImage(image: Uri) {
        container.onImageChanged?.invoke(image)
    }
}