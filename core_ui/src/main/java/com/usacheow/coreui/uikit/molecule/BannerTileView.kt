package com.usacheow.coreui.uikit.molecule

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.facebook.shimmer.ShimmerFrameLayout
import com.usacheow.coreui.R
import com.usacheow.coreui.adapters.base.Populatable
import com.usacheow.coreui.adapters.base.ViewType
import com.usacheow.coreui.databinding.ViewBannerTileBinding
import com.usacheow.coreui.utils.*
import com.usacheow.coreui.utils.view.*

private const val TEXT_SHIMMER_WIDTH_DP = 144

class BannerTileView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ShimmerFrameLayout(context, attrs, defStyleAttr), Populatable<BannerTileItem> {

    private val binding by lazy { ViewBannerTileBinding.bind(this) }

    private val textShimmerWidthPx by lazy { TEXT_SHIMMER_WIDTH_DP.toPx }

    override fun populate(model: BannerTileItem) {
        binding.bannerClickableView.setListenerIfNeed(model.isShimmer, model.clickListener)

        updateMargins(model.margin)
        populateIcon(model)
        populateText(model)
        setShimmer(model.isShimmer)
    }

    private fun populateText(model: BannerTileItem) = with (binding.bannerTextView) {
        if (model.isShimmer) {
            showShimmer(widthPx = textShimmerWidthPx)
        } else {
            hideShimmer(widthPx = ViewGroup.LayoutParams.WRAP_CONTENT)
            populate(model.text)
        }
    }

    private fun populateIcon(model: BannerTileItem) = with (binding.bannerIconView) {
        if (model.isShimmer) {
            showCircleShimmer()
        } else {
            hideShimmer(model.icon)
        }
    }
}

data class BannerTileItem(
    val icon: ImageSource,
    val text: TextSource,
    val margin: Margin = Margin2(8.toPx, 8.toPx),
    val clickListener: (() -> Unit)? = null,
) : ViewType(R.layout.view_banner_tile) {

    companion object {
        fun shimmer() = BannerTileItem(icon = ImageEmpty, text = TextString("")).apply { isShimmer = true }
    }
}