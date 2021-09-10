package com.sarftec.lifelessons.application.viewmodel

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sarftec.lifelessons.application.activity.BaseActivity
import com.sarftec.lifelessons.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ListViewModel @Inject constructor(
   repository: Repository,
    private val stateHandle: SavedStateHandle
) : BaseListViewModel(repository) {

    override fun getToolbarTitle(): String? {
        return stateHandle.get<Bundle>("bundle")?.getString(BaseActivity.CATEGORY_SELECTED_NAME)
            ?.let {
                "$it Lessons"
            }
    }

    override fun fetch() {
        if(_quotes.value != null) return
        viewModelScope.launch {
            stateHandle.get<Bundle>("bundle")?.let { bundle ->
                bundle.getString(BaseActivity.CATEGORY_SELECTED_NAME)?.let {
                    val fetchedQuotes = repository.database().quoteDao().quotes(it)
                    val seed = (0 until 100).random()
                    stateHandle.set("seed", seed)
                    _quotes.value = fetchedQuotes.shuffled(Random(seed))
                }
            }
        }
    }

    fun getSeed(): Int {
        return stateHandle.get<Int>("seed") ?: 0
    }

    fun setBundle(bundle: Bundle) {
        stateHandle.set("bundle", bundle)
    }
}