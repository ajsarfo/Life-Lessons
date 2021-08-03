package com.sarftec.lifelessons.application

import android.net.Uri
import com.sarftec.lifelessons.data.database.entity.Category
import com.sarftec.lifelessons.data.database.entity.Quote
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Container @Inject constructor() {
    var onImageChanged: ((Uri) -> Unit)? = null
    var onFavoriteChanged: ((Boolean) -> Unit)? = null
    var selectedQuote: Quote? = null
    var selectedCategory: Category? = null
    var selectedImageUri: Uri? = null

    fun reset() {
        onImageChanged = null
        onFavoriteChanged = null
        selectedCategory = null
        selectedImageUri = null
    }
}