package com.sarftec.lifelessons.application.adapter

import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lifelessons.application.viewmodel.PanelState
import com.sarftec.lifelessons.application.viewmodel.QuoteAlignment
import com.sarftec.lifelessons.data.database.entity.Quote
import com.sarftec.lifelessons.databinding.LayoutQuotePagerViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class QuotePagerHolder(
    private val layoutBinding: LayoutQuotePagerViewBinding,
) : RecyclerView.ViewHolder(layoutBinding.root), QuotePagerAdapter.PagerHolderListener {

    private var job: Job? = null


    fun bind(quote: Quote) {
        layoutBinding.pagerText.text = quote.message
    }

    override fun notifyPanelStateChanged(state: PanelState) {
        with(layoutBinding.pagerText) {
            setTextColor(state.color)
            if(state.size != -1f) textSize = state.size
            if(state.fontLocation.isNotEmpty()) typeface = Typeface.createFromAsset(itemView.context.assets, state.fontLocation)
            gravity = when(state.alignment) {
                QuoteAlignment.END -> Gravity.END
                QuoteAlignment.START ->  Gravity.START
                QuoteAlignment.CENTER -> Gravity.CENTER
            }
            isAllCaps = state.isAllCaps
            paintFlags = if (state.isUnderlined) Paint.UNDERLINE_TEXT_FLAG
            else paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
        }
    }
}