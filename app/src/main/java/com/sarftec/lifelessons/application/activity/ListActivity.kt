package com.sarftec.lifelessons.application.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.appodeal.ads.Appodeal
import com.sarftec.lifelessons.application.ACTIVITY_BUNDLE
import com.sarftec.lifelessons.application.adapter.ListItemAdapter
import com.sarftec.lifelessons.application.dialog.LoadingDialog
import com.sarftec.lifelessons.application.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class ListActivity : BaseListActivity() {

    override val viewModel by viewModels<ListViewModel>()

    private val listItemAdapter by lazy {
        ListItemAdapter(dependency, viewModel)
    }

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }

    override fun configureAdapter(recyclerView: RecyclerView) {
        recyclerView.adapter  = listItemAdapter
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
            delay(1000)
            loadingDialog.dismiss()
        }
    }


    override fun onResume() {
        super.onResume()
        Appodeal.show(this, Appodeal.BANNER_VIEW)
    }
}