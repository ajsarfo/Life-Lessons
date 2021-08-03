package com.sarftec.lifelessons.application.viewmodel

import androidx.lifecycle.viewModelScope
import com.sarftec.lifelessons.application.ApplicationScope
import com.sarftec.lifelessons.application.Container
import com.sarftec.lifelessons.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: Repository,
    container: Container,
    applicationScope: ApplicationScope
) : BaseListViewModel(container, applicationScope) {

    override fun fetch() {
        viewModelScope.launch {
            _quotes.value = repository.database().quoteDao().favorites()
        }
    }

    override fun getToolbarTitle(): String {
        return "Favorite Lessons"
    }
}