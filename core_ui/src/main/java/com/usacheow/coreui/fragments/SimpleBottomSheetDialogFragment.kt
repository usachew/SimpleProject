package com.usacheow.coreui.fragments

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.core.view.updateLayoutParams
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.usacheow.coreui.R
import com.usacheow.coreui.analytics.AnalyticsTrackerHolder
import com.usacheow.coreui.analytics.Events
import com.usacheow.coreui.base.SimpleLifecycle
import com.usacheow.coreui.delegate.ViewBindingDelegate
import com.usacheow.coreui.utils.view.toPx

abstract class SimpleBottomSheetDialogFragment<VIEW_BINDING : ViewBinding>  : BottomSheetDialogFragment(), SimpleLifecycle {

    protected abstract val params: Params<VIEW_BINDING>

    protected val binding get() = viewBindingDelegate.binding
    private val viewBindingDelegate by lazy { ViewBindingDelegate<VIEW_BINDING>() }
    private val viewBindingProvider get() = params.viewBindingProvider

    private val canHide get() = params.canHide
    private val needWrapContent get() = params.needWrapContent
    private val needExpand get() = params.needExpand

    private val middleStatePercent get() = params.middleStatePercent
    private val needMiddleState get() = params.needMiddleState

    private val startStatePercent get() = params.startStatePercent

    @CallSuper
    override fun onStart() {
        super.onStart()
        AnalyticsTrackerHolder.getInstance()?.trackEvent(Events.START_SCREEN, this.javaClass)
    }

    @CallSuper
    override fun onStop() {
        AnalyticsTrackerHolder.getInstance()?.trackEvent(Events.STOP_SCREEN, this.javaClass)
        super.onStop()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<FrameLayout>(R.id.design_bottom_sheet)
            bottomSheet?.updateLayoutParams<ViewGroup.LayoutParams> { height = ViewGroup.LayoutParams.MATCH_PARENT }
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)

            bottomSheetBehavior.isHideable = canHide

            bottomSheetBehavior.isFitToContents = needWrapContent
            if (needWrapContent) return@setOnShowListener

            if (needExpand) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            bottomSheetBehavior.peekHeight = getPeekHeight()
            bottomSheetBehavior.halfExpandedRatio = when {
                needMiddleState -> middleStatePercent.divisor
                else -> startStatePercent.divisor
            }
        }

        return bottomSheetDialog
    }

    private fun getPeekHeight() = (startStatePercent.divisor * (Resources.getSystem().displayMetrics.heightPixels - 0.toPx)).toInt()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBindingDelegate.save(viewBindingProvider(inflater, container, false))
        return viewBindingDelegate.rootView
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        processArguments(arguments)
        setupViews(savedInstanceState)
        subscribe()
    }

    @CallSuper
    override fun onDestroyView() {
        clearViews()
        viewBindingDelegate.clear()
        super.onDestroyView()
    }

    data class Params<VIEW_BINDING : ViewBinding>(
            var canHide: Boolean = true,
            var needWrapContent: Boolean = false,
            var needExpand: Boolean = false,

            /*
            * halfExpandedRatio value when needMiddleState
            * */
            var middleStatePercent: BottomDialogHeight = BottomDialogHeight.HALF_SIZE,
            var needMiddleState: Boolean = false,

            /*
            * peekHeight value
            * */
            var startStatePercent: BottomDialogHeight = BottomDialogHeight.QUARTER_SIZE,

            val viewBindingProvider: (LayoutInflater, ViewGroup?, Boolean) -> VIEW_BINDING,
    )

    enum class BottomDialogHeight(val divisor: Float) {

        THREE_QUARTERS_SIZE(0.75f),
        HALF_SIZE(0.5f),
        QUARTER_SIZE(0.25f)
    }
}