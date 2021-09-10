package com.sarftec.lifelessons.application.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lifelessons.application.Dependency
import com.sarftec.lifelessons.application.model.MainItem
import com.sarftec.lifelessons.application.viewmodel.MainViewModel
import com.sarftec.lifelessons.data.database.entity.Category
import com.sarftec.lifelessons.databinding.LayoutMainItemBinding

class MainItemAdapter(
    dependency: Dependency,
    viewModel: MainViewModel,
    private var items: List<MainItem> = emptyList(),
    private val onClick: (MainItem) -> Unit
    ) : RecyclerView.Adapter<MainItemViewHolder>() {

    private val capsule = Capsule(dependency, viewModel, onClick)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainItemViewHolder {
        val binding = LayoutMainItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MainItemViewHolder(capsule, binding)
    }

    override fun onBindViewHolder(holder: MainItemViewHolder, position: Int) {
       holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitData(data: List<MainItem>) {
        items = data
        notifyDataSetChanged()
    }

    class Capsule(val dependency: Dependency, val viewModel: MainViewModel, val onClick: (MainItem) -> Unit)
}