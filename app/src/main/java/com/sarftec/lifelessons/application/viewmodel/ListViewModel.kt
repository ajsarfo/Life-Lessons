package com.sarftec.lifelessons.application.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sarftec.lifelessons.application.ApplicationScope
import com.sarftec.lifelessons.application.Container
import com.sarftec.lifelessons.application.SELECTED_CATEGORY
import com.sarftec.lifelessons.data.database.entity.Quote
import com.sarftec.lifelessons.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: Repository,
    container: Container,
    applicationScope: ApplicationScope
) : BaseListViewModel(container, applicationScope) {

    private var persistedBundle: Bundle? = null

    override fun getToolbarTitle(): String? {
        return persistedBundle?.getString(SELECTED_CATEGORY)?.let {
            "$it Lessons"
        }
    }

    override fun fetch() {
        viewModelScope.launch {
            persistedBundle?.getString(SELECTED_CATEGORY)?.let {
                _quotes.value = repository.database().quoteDao().quotes(it).shuffled()
            }
        }
    }


    fun setBundle(bundle: Bundle) {
        persistedBundle = bundle
    }

    fun getBundle(): Bundle? = persistedBundle
}