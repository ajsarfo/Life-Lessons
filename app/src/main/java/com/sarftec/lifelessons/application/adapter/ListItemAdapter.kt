package com.sarftec.lifelessons.application.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lifelessons.application.Dependency
import com.sarftec.lifelessons.application.viewmodel.BaseListViewModel
import com.sarftec.lifelessons.data.database.entity.Quote
import com.sarftec.lifelessons.databinding.LayoutListItemBinding

class ListItemAdapter(
    dependency: Dependency,
    viewModel: BaseListViewModel,
    private var items: List<Quote> = emptyList()
) : RecyclerView.Adapter<ListItemViewHolder>() {

    private val imageCache = ImageCache(dependency)
    private val capsule = Capsule(dependency, imageCache, viewModel)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = LayoutListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bind(items[position], position, capsule)
    }

    override fun getItemCount(): Int = items.size

    fun submitData(items: List<Quote>) {
        this.items = items
        notifyDataSetChanged()
    }
}