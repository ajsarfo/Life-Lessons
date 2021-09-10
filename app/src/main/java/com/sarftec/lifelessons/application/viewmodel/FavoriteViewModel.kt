package com.sarftec.lifelessons.application.viewmodel

import androidx.lifecycle.viewModelScope
import com.sarftec.lifelessons.data.database.entity.Quote
import com.sarftec.lifelessons.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    repository: Repository
) : BaseListViewModel(repository) {

    override fun fetch() {
        if(_quotes.value != null) return
        viewModelScope.launch {
            _quotes.value = repository.database().quoteDao().favorites()
        }
    }

    override fun getToolbarTitle(): String {
        return "Favorite Lessons"
    }
}