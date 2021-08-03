package com.sarftec.lifelessons.application.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.sarftec.lifelessons.R
import com.sarftec.lifelessons.application.binding.QuoteBinding
import com.sarftec.lifelessons.application.viewmodel.QuoteViewModel
import com.sarftec.lifelessons.databinding.ActivityQuoteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuoteActivity : BaseActivity() {

    private val binding by lazy {
        ActivityQuoteBinding.inflate(
            LayoutInflater.from(this)
        )
    }

    private val viewModel by viewModels<QuoteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //Setting status config
        statusColor(ContextCompat.getColor(this, R.color.color_primary))
        viewModel.listItem?.let {
            binding.binding = QuoteBinding(dependency, it, viewModel).apply {
                init()
            }
            binding.executePendingBindings()
        }
    }
}