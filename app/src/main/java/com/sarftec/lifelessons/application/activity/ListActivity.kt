package com.sarftec.lifelessons.application.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lifelessons.R
import com.sarftec.lifelessons.application.adapter.ListItemAdapter
import com.sarftec.lifelessons.application.dialog.LoadingDialog
import com.sarftec.lifelessons.application.file.vibrate
import com.sarftec.lifelessons.application.manager.AdCountManager
import com.sarftec.lifelessons.application.tools.PermissionHandler
import com.sarftec.lifelessons.application.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class ListActivity : BaseListActivity() {

    override val viewModel by viewModels<ListViewModel>()

    private val permissionHandler by lazy {
        PermissionHandler(this)
    }

    private val listItemAdapter by lazy {
        ListItemAdapter(dependency, viewModel, permissionHandler) { quote, imageUri ->
            vibrate()
            navigateTo(
                QuoteActivity::class.java,
                bundle = Bundle().apply {
                    putString(CATEGORY_SELECTED_NAME, quote.category)
                    putInt(QUOTE_SELECTED_ID, quote.id)
                    putInt(RANDOM_SEED, viewModel.getSeed())
                    putString(NAVIGATION_ROOT, NAVIGATION_QUOTE_LIST)
                    putString(QUOTE_SELECTED_IMAGE, imageUri.toString())
                }
            )
        }
    }

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }

    override fun createAdCounterManager(): AdCountManager {
        return AdCountManager(listOf(2, 3, 2, 2, 3, 4))
    }

    override fun configureAdapter(recyclerView: RecyclerView) {
        recyclerView.adapter = listItemAdapter
    }

    override fun getBannerId(): String {
        return getString(R.string.admob_banner_list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog.show()
        savedInstanceState ?: kotlin.run {
            intent.getBundleExtra(ACTIVITY_BUNDLE)?.let {
                viewModel.setBundle(it)
            }
        }
        with(binding.listToolbar) {
            title = viewModel.getToolbarTitle()
        }
        viewModel.fetch()
        viewModel.quotes.observe(this) {
            listItemAdapter.submitData(it)
        }
        lifecycleScope.launchWhenCreated {
            delay(800)
            loadingDialog.dismiss()
        }
    }


    override fun onResume() {
        super.onResume()
        listItemAdapter.resetQuoteFavorites(modifiedQuoteList.entries)
        modifiedQuoteList.clear()
    }
}