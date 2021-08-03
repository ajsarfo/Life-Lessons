package com.sarftec.lifelessons.application.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lifelessons.application.adapter.ListItemAdapter
import com.sarftec.lifelessons.application.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteActivity : BaseListActivity() {

    override val viewModel by viewModels<FavoriteViewModel>()

    private val favoriteItemAdapter by lazy {
       ListItemAdapter(dependency, viewModel)
    }

    override fun configureAdapter(recyclerView: RecyclerView) {
        recyclerView.adapter = favoriteItemAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetch()
        viewModel.quotes.observe(this) {
            if(it.isEmpty()) {
                binding.noFavoriteLayout.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
            favoriteItemAdapter.submitData(it)
        }
    }
}