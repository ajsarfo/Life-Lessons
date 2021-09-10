package com.sarftec.lifelessons.application.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lifelessons.application.adapter.ListItemAdapter
import com.sarftec.lifelessons.application.tools.PermissionHandler
import com.sarftec.lifelessons.application.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteActivity : BaseListActivity() {

    override val viewModel by viewModels<FavoriteViewModel>()

    private val permissionHandler by lazy {
        PermissionHandler(this)
    }

    private val favoriteItemAdapter by lazy {
       ListItemAdapter(dependency, viewModel, permissionHandler) { quote, imageUri ->
           navigateTo(
               QuoteActivity::class.java,
               bundle = Bundle().apply {
                   putString(CATEGORY_SELECTED_NAME, quote.category)
                   putInt(QUOTE_SELECTED_ID, quote.id)
                   putString(NAVIGATION_ROOT, NAVIGATION_FAVORITE_LIST)
                   putString(QUOTE_SELECTED_IMAGE, imageUri.toString())
               }
           )
       }
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

    override fun onResume() {
        super.onResume()
        favoriteItemAdapter.resetQuoteFavorites(modifiedQuoteList.entries)
        modifiedQuoteList.clear()
    }
}