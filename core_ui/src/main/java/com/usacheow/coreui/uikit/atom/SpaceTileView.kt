package com.usacheow.coreui.uikit.atom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.usacheow.coreui.R
import com.usacheow.coreui.adapters.base.Populatable
import com.usacheow.coreui.adapters.base.ViewType
import com.usacheow.coreui.utils.view.toPx

class SpaceTileView
@JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attributeSet, defStyleAttr), Populatable<SpaceTileItem> {

    override fun populate(model: SpaceTileItem) {
        layoutParams = layoutParams.apply { height = model.heightDp.toPx }
    }
}

data class SpaceTileItem(
    val heightDp: Int,
) : ViewType(R.layout.view_space_tile)