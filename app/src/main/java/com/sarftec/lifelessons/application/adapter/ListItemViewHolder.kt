package com.sarftec.lifelessons.application.adapter

import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lifelessons.application.binding.ListItemBinding
import com.sarftec.lifelessons.data.database.entity.Quote
import com.sarftec.lifelessons.databinding.LayoutListItemBinding

class ListItemViewHolder(
    private val binding: LayoutListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(quote: Quote, position: Int, capsule: Capsule) {
        binding.binding = ListItemBinding(quote, position, capsule).also {
            it.init()
        }
    }
}