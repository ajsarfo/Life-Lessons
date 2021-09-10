package com.sarftec.lifelessons.application.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sarftec.lifelessons.R
import com.sarftec.lifelessons.application.viewmodel.BaseListViewModel
import com.sarftec.lifelessons.databinding.ActivityListBinding

abstract class BaseListActivity : BaseActivity() {

    protected val binding by lazy {
        ActivityListBinding.inflate(
            LayoutInflater.from(this)
        )
    }

    protected abstract val viewModel: BaseListViewModel

    protected abstract fun configureAdapter(recyclerView: RecyclerView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Setting status config
        statusColor(ContextCompat.getColor(this, R.color.color_primary))
        setContentView(binding.root)
        with(binding.listToolbar) {
            title = viewModel.getToolbarTitle()
            setNavigationOnClickListener { onBackPressed() }
        }
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(this@BaseListActivity)
            setHasFixedSize(true)
            configureAdapter(binding.recyclerView)
        }
    }
}