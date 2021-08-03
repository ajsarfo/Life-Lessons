package com.sarftec.lifelessons.application.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sarftec.lifelessons.application.ApplicationScope
import com.sarftec.lifelessons.application.Container
import com.sarftec.lifelessons.data.database.entity.Quote

abstract class BaseListViewModel(
    private val container: Container,
    private val applicationScope: ApplicationScope,
) : ViewModel() {

    protected val _quotes = MutableLiveData<List<Quote>>()
    val quotes: LiveData<List<Quote>>
        get() = _quotes

    abstract fun getToolbarTitle(): String?

    abstract fun fetch()

    fun saveItem(item: Quote) {
        applicationScope.saveQuote(item)
    }

    fun persistCallbacks(onImageChanged: (Uri) -> Unit, onFavoriteChanged: (Boolean) -> Unit) {
        container.onImageChanged = onImageChanged
        container.onFavoriteChanged = onFavoriteChanged
    }

    fun persistItem(item: Quote, image: Uri) {
        container.selectedQuote = item
        container.selectedImageUri = image
    }
}