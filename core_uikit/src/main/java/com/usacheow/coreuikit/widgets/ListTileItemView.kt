package com.usacheow.coreuikit.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.facebook.shimmer.ShimmerFrameLayout
import com.usacheow.coreuikit.R
import com.usacheow.coreuikit.base.Populatable
import com.usacheow.coreuikit.base.ViewType
import com.usacheow.coreuikit.utils.ext.makeGone
import com.usacheow.coreuikit.utils.ext.makeVisible
import com.usacheow.coreuikit.utils.ext.populate
import com.usacheow.coreuikit.utils.ext.resize
import com.usacheow.coreuikit.utils.ext.setListenerIfNeed
import com.usacheow.coreuikit.utils.ext.toPx
import kotlinx.android.synthetic.main.list_tile_item_view.view.actionDescriptionView
import kotlinx.android.synthetic.main.list_tile_item_view.view.actionIconView
import kotlinx.android.synthetic.main.list_tile_item_view.view.actionSubtitleView
import kotlinx.android.synthetic.main.list_tile_item_view.view.actionTitleView

private const val SUBTITLE_SHIMMER_WIDTH_DP = 120
private const val TITLE_SHIMMER_WIDTH_DP = 200

class ListTileItemView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ShimmerFrameLayout(context, attrs, defStyleAttr), Populatable<ListTileItem> {

    override fun populate(model: ListTileItem) {
        if (model.isShimmer) {
            actionTitleView.setBackgroundResource(R.drawable.bg_shimmer_line)
            actionTitleView.resize(widthPx = TITLE_SHIMMER_WIDTH_DP.toPx, heightPx = ViewGroup.LayoutParams.WRAP_CONTENT)
            actionSubtitleView.setBackgroundResource(R.drawable.bg_shimmer_line)
            actionSubtitleView.resize(widthPx = SUBTITLE_SHIMMER_WIDTH_DP.toPx, heightPx = ViewGroup.LayoutParams.WRAP_CONTENT)
            actionIconView.setImageResource(R.drawable.bg_shimmer_circle)
            actionDescriptionView.makeGone()

            showShimmer(true)
        } else {
            actionTitleView.background = null
            actionTitleView.resize(widthPx = ViewGroup.LayoutParams.MATCH_PARENT, heightPx = ViewGroup.LayoutParams.WRAP_CONTENT)
            actionSubtitleView.background = null
            actionSubtitleView.resize(widthPx = ViewGroup.LayoutParams.MATCH_PARENT, heightPx = ViewGroup.LayoutParams.WRAP_CONTENT)
            actionIconView.setImageDrawable(null)
            actionDescriptionView.makeVisible()

            hideShimmer()
            showData(model)
        }
    }

    private fun showData(model: ListTileItem) {
        actionTitleView.text = model.title
        actionSubtitleView.populate(model.subtitle)
        actionDescriptionView.populate(model.description)
        actionIconView.populate(model.imageInfo)
        setListenerIfNeed(model.onItemClicked)
    }
}

data class ListTileItem(
    val imageInfo: ImageInfo = EmptyState(),
    val title: String? = null,
    val subtitle: String,
    val description: String? = null,
    val onItemClicked: (() -> Unit)? = null
) : ViewType(R.layout.list_tile_item_view) {

    companion object {
        fun shimmer() = ListTileItem(subtitle = "").apply { isShimmer = true }
    }
}