package com.kapmayn.coreuikit.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.kapmayn.coreuikit.R
import com.kapmayn.coreuikit.base.Populatable
import com.kapmayn.coreuikit.base.ViewType
import com.kapmayn.coreuikit.utils.populate
import kotlinx.android.synthetic.main.view_header_with_action_item.view.actionButton
import kotlinx.android.synthetic.main.view_header_with_action_item.view.headerView

class HeaderWithActionView
@JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attributeSet, defStyleAttr), Populatable<HeaderWithActionItem> {

    override fun populate(model: HeaderWithActionItem) {
        headerView.text = model.title
        actionButton.populate(model.actionTitle)
        actionButton.setOnClickListener {
            model.clickAction?.invoke()
        }
    }
}

data class HeaderWithActionItem(
    val title: String,
    val actionTitle: String? = null,
    val clickAction: (() -> Unit)? = null
) : ViewType(R.layout.view_header_with_action_item)