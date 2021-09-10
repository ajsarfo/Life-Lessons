package com.sarftec.lifelessons.application.adapter

import android.net.Uri
import com.sarftec.lifelessons.application.Dependency
import com.sarftec.lifelessons.application.tools.PermissionHandler
import com.sarftec.lifelessons.application.viewmodel.BaseListViewModel
import com.sarftec.lifelessons.data.database.entity.Quote

class Capsule(
    val dependency: Dependency,
    val imageCache: ImageCache,
    val viewModel: BaseListViewModel,
    val onClick: (Quote, Uri) -> Unit
)