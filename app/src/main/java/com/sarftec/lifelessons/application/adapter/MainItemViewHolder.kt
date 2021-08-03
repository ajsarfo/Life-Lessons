package com.sarftec.lifelessons.application.adapter

import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lifelessons.application.binding.MainItemBinding
import com.sarftec.lifelessons.application.model.MainItem
import com.sarftec.lifelessons.databinding.LayoutMainItemBinding

class MainItemViewHolder(
    private val capsule: MainItemAdapter.Capsule,
    private val binding: LayoutMainItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: MainItem) {
        binding.binding = MainItemBinding(capsule, model).also { it.init() }
        binding.executePendingBindings()
    }
}