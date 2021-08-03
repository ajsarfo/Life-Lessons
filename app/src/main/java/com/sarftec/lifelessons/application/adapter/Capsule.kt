package com.sarftec.lifelessons.application.adapter

import com.sarftec.lifelessons.application.Dependency
import com.sarftec.lifelessons.application.viewmodel.BaseListViewModel

class Capsule(
    val dependency: Dependency,
    val imageCache: ImageCache,
    val viewModel: BaseListViewModel
)