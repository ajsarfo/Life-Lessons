package com.sarftec.lifelessons.application.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lifelessons.application.Dependency
import com.sarftec.lifelessons.application.tools.PermissionHandler
import com.sarftec.lifelessons.application.viewmodel.BaseListViewModel
import com.sarftec.lifelessons.data.database.entity.Quote
import com.sarftec.lifelessons.databinding.LayoutListItemBinding

class ListItemAdapter(
    dependency: Dependency,
    viewModel: BaseListViewModel,
    private val permissionHandler: PermissionHandler,
    private var items: List<Quote> = emptyList(),
    private val onClick: (Quote, Uri) -> Unit
) : RecyclerView.Adapter<ListItemViewHolder>() {

    private val imageCache = ImageCache(dependency)
    private val capsule = Capsule(dependency, imageCache, viewModel, onClick)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = LayoutListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListItemViewHolder(binding, permissionHandler)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bind(items[position], position, capsule)
    }

    override fun getItemCount(): Int = items.size

    fun resetQuoteFavorites(indexedFavorites: Set<Map.Entry<Int, Boolean>>) {
        indexedFavorites.forEach {
            items[it.key].favorite = it.value
            notifyItemChanged(it.key)
        }
    }

    fun submitData(items: List<Quote>) {
        this.items = items
        notifyDataSetChanged()
    }
}