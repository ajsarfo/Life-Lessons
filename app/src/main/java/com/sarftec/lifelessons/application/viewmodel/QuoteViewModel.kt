package com.sarftec.lifelessons.application.viewmodel

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.*
import com.sarftec.lifelessons.application.activity.BaseActivity
import com.sarftec.lifelessons.application.panel.AlignmentManager
import com.sarftec.lifelessons.application.panel.PanelListener
import com.sarftec.lifelessons.data.database.entity.Quote
import com.sarftec.lifelessons.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

enum class QuoteAlignment { START, CENTER, END }

class PanelState(
    var color: Int = Color.WHITE,
    var opacity: Int = -1,
    var size: Float = -1f,
    var fontLocation: String = "",
    var isAllCaps: Boolean = false,
    var isUnderlined: Boolean = false,
    var alignment: QuoteAlignment = QuoteAlignment.CENTER
) {
    private var changeSet: Boolean = true

    override fun equals(other: Any?): Boolean {
        if (other !is PanelState) return false
        return other.changeSet == this.changeSet
    }

    override fun hashCode(): Int {
        return if (changeSet) 1 else 0
    }

    fun switch(): PanelState {
        changeSet = !changeSet
        return this
    }
}

class IndexedQuotes(
    var index: Int,
    val quotes: List<Quote>
) {
    override fun equals(other: Any?): Boolean {
        if (other !is IndexedQuotes) return false
        return other.index == index
    }

    override fun hashCode(): Int {
        var result = index
        result = 31 * result + quotes.hashCode()
        return result
    }
}

sealed class QuoteBackground {
    class BackgroundImage(val path: String) : QuoteBackground()
    class BackgroundColor(val color: Int) : QuoteBackground()
    object None : QuoteBackground()
}

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val repository: Repository,
    private val stateHandle: SavedStateHandle
) : ViewModel(), PanelListener {

    private val _indexedQuotes = MutableLiveData<IndexedQuotes>()
    val indexedQuotes: LiveData<IndexedQuotes>
        get() = _indexedQuotes

    private val _currentQuote = MutableLiveData<Quote>()
    val currentQuote: LiveData<Quote>
        get() = _currentQuote


    private val _panelState = MutableLiveData(PanelState())
    val panelState: LiveData<PanelState>
        get() = _panelState

    fun fetch() {
        if (indexedQuotes.value != null) return
        viewModelScope.launch {
            stateHandle.get<Bundle>("bundle")?.let { bundle ->
                bundle.getString(BaseActivity.NAVIGATION_ROOT)?.let {
                    val quoteList = if (it == BaseActivity.NAVIGATION_QUOTE_LIST) {
                        repository.database().quoteDao().quotes(
                            bundle.getString(BaseActivity.CATEGORY_SELECTED_NAME)!!
                        ).shuffled(Random(bundle.getInt(BaseActivity.RANDOM_SEED)))
                    } else {
                        repository.database().quoteDao().favorites()
                    }
                    var index = stateHandle.get<Int>(CURRENT_INDEX) ?: -1
                    if (index == -1) {
                        index = quoteList.indexOfFirst { quote ->
                            quote.id == bundle.getInt(BaseActivity.QUOTE_SELECTED_ID)
                        }
                    }
                    _indexedQuotes.value = IndexedQuotes(index, quoteList)
                    setCurrentQuoteIndex(index)
                }
            }
        }
    }

    fun getCurrentQuote(): Quote? {
        val index = stateHandle.get<Int>(CURRENT_INDEX) ?: -1
        if (index == -1) return null
        return _indexedQuotes.value?.quotes?.get(index)
    }

    fun getQuoteBackground(): QuoteBackground {
        return stateHandle.get<String>(BACKGROUND_OPTION)?.let { option ->
            when (option) {
                IS_IMAGE -> {
                    stateHandle.get<String>(IMAGE_PATH)?.let {
                        QuoteBackground.BackgroundImage(it)
                    } ?: QuoteBackground.None
                }
                IS_COLOR -> {
                    stateHandle.get<Int>(COLOR_CODE)?.let {
                        QuoteBackground.BackgroundColor(it)
                    } ?: QuoteBackground.None
                }
                else -> QuoteBackground.None
            }
        } ?: QuoteBackground.None
    }

    fun setBackgroundImage(imageUri: Uri) {
        stateHandle.set(BACKGROUND_OPTION, IS_IMAGE)
        stateHandle.set(IMAGE_PATH, imageUri.toString())
    }

    fun setBackgroundColor(color: Int) {
        stateHandle.set(BACKGROUND_OPTION, IS_IMAGE)
        stateHandle.set(COLOR_CODE, color)
    }

    fun setCurrentQuoteIndex(index: Int) {
        stateHandle.set(CURRENT_INDEX, index)
        _indexedQuotes.value?.let {
            _currentQuote.value = it.quotes[index]
        }
    }

    fun changeCurrentQuoteFavorite(): Quote? {
        val current = _currentQuote.value ?: return null
        current.favorite = !current.favorite
        stateHandle.get<Int>(CURRENT_INDEX)?.let { index ->
            BaseActivity.modifiedQuoteList
                .entries
                .firstOrNull { it.key == index }
                ?.setValue(current.favorite) ?: kotlin.run {
                BaseActivity.modifiedQuoteList.put(index, current.favorite)
            }
        }
        viewModelScope.launch {
            repository.database().quoteDao().update(current.id, current.favorite)
        }
        _currentQuote.value = current
        return current
    }

    fun setBundle(bundle: Bundle) {
        stateHandle.set("bundle", bundle)
        bundle.getString(BaseActivity.QUOTE_SELECTED_IMAGE)?.let {
            setBackgroundImage(Uri.parse(it))
        }
    }

    private fun updatePanelState() {
        _panelState.value = _panelState.value?.switch()
    }

    /**
     ** This block of code implement panel listener
     **/
    override fun setFont(fontLocation: String) {
        _panelState.value?.fontLocation = fontLocation
        updatePanelState()
    }

    override fun setColor(color: Int) {
        _panelState.value?.color = color
        updatePanelState()
    }

    override fun setOpacity(color: Int) {
        _panelState.value?.opacity = color
        updatePanelState()
    }

    override fun setSize(size: Float) {
        _panelState.value?.size = size
        updatePanelState()
    }

    override fun setAlignment(position: AlignmentManager.Position) {
        _panelState.value?.alignment = when (position) {
            AlignmentManager.Position.CENTER -> QuoteAlignment.CENTER
            AlignmentManager.Position.LEFT -> QuoteAlignment.START
            AlignmentManager.Position.RIGHT -> QuoteAlignment.END
        }
        updatePanelState()
    }

    override fun setAllCaps(option: AlignmentManager.Option) {
        _panelState.value?.isAllCaps = option == AlignmentManager.Option.YES
        updatePanelState()
    }

    override fun setUnderlined(option: AlignmentManager.Option) {
        _panelState.value?.isUnderlined = option == AlignmentManager.Option.YES
        updatePanelState()
    }

    override fun getAlignment(): AlignmentManager.Position {
        if (_panelState.value == null) AlignmentManager.Position.CENTER
        return when (_panelState.value!!.alignment) {
            QuoteAlignment.CENTER -> AlignmentManager.Position.CENTER
            QuoteAlignment.START -> AlignmentManager.Position.LEFT
            QuoteAlignment.END -> AlignmentManager.Position.RIGHT
        }
    }

    override fun isAllCaps(): AlignmentManager.Option {
        return _panelState.value?.isAllCaps?.let {
            if (it) AlignmentManager.Option.YES else AlignmentManager.Option.NO
        } ?: AlignmentManager.Option.NO
    }

    override fun isUnderlined(): AlignmentManager.Option {
        return _panelState.value?.isUnderlined?.let {
            if (it) AlignmentManager.Option.YES else AlignmentManager.Option.NO
        } ?: AlignmentManager.Option.NO
    }

    companion object {
        const val CURRENT_INDEX = "detail_current_index"
        const val BACKGROUND_OPTION = "background_option"
        const val IMAGE_PATH = "image_path"
        const val COLOR_CODE = "color_code"
        const val IS_COLOR = "is_color"
        const val IS_IMAGE = "is_image"
    }
}