package com.sarftec.lifelessons.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarftec.lifelessons.data.database.entity.Quote
import com.sarftec.lifelessons.data.repository.Repository
import kotlinx.coroutines.launch

abstract class BaseListViewModel(
    protected val repository: Repository
) : ViewModel() {

    protected val _quotes = MutableLiveData<List<Quote>>()
    val quotes: LiveData<List<Quote>>
        get() = _quotes

    abstract fun getToolbarTitle(): String?

    abstract fun fetch()

    fun saveItem(item: Quote) {
        viewModelScope.launch {
            repository.database().quoteDao().update(
                item.id, item.favorite
            )
        }
    }
}