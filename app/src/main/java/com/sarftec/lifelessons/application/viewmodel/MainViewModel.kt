package com.sarftec.lifelessons.application.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarftec.lifelessons.application.Container
import com.sarftec.lifelessons.application.SELECTED_CATEGORY
import com.sarftec.lifelessons.application.SELECTED_CATEGORY_SIZE
import com.sarftec.lifelessons.application.model.MainItem
import com.sarftec.lifelessons.data.database.entity.Category
import com.sarftec.lifelessons.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repository: Repository
) : ViewModel() {

    private var persistableBundle: Bundle? = null

    private val _quotes = MutableLiveData<List<MainItem>>()
    val quotes: LiveData<List<MainItem>>
        get() = _quotes

    init {
        viewModelScope.launch {
            with(repository.database()) {
                _quotes.value = categoryDao().categories()
                    .map {
                        MainItem(
                            category = it.category,
                            randomQuote = quoteDao().random(it.category).message,
                            size = it.size
                        )
                    }
            }
        }
    }

    fun persistItem(item: MainItem) {
       persistableBundle = Bundle().apply {
            putString(SELECTED_CATEGORY, item.category)
            putInt(SELECTED_CATEGORY_SIZE, item.size)
        }
    }

    fun getBundle() : Bundle? {
        return persistableBundle
    }
}