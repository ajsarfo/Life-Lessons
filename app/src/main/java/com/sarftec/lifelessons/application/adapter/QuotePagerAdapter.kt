package com.sarftec.lifelessons.application.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lifelessons.application.viewmodel.PanelState
import com.sarftec.lifelessons.data.database.entity.Quote
import com.sarftec.lifelessons.databinding.LayoutQuotePagerViewBinding
import kotlinx.coroutines.CoroutineScope

class QuotePagerAdapter(
    private val coroutineScope: CoroutineScope,
    private var items: List<Quote> = emptyList()

) : RecyclerView.Adapter<QuotePagerHolder>() {

    private val panelStateListeners = hashSetOf<PagerHolderListener>()

    private var panelState: PanelState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotePagerHolder {
        val layoutBinding = LayoutQuotePagerViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuotePagerHolder(layoutBinding).also {
            panelStateListeners.add(it)
        }
    }

    override fun onBindViewHolder(holder: QuotePagerHolder, position: Int) {
        panelStateListeners.add(holder)
        holder.bind(items[position])
        panelState?.let {
            holder.notifyPanelStateChanged(it)
        }
    }

    override fun getItemCount(): Int = items.size

    fun changePanelState(state: PanelState) {
        panelState = state
        panelStateListeners.forEach {
            it.notifyPanelStateChanged(state)
        }
    }

    fun submitData(items: List<Quote>) {
        this.items = items
        notifyDataSetChanged()
    }

    interface PagerHolderListener {
        fun notifyPanelStateChanged(state: PanelState)
    }
}