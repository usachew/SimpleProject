package com.kapmayn.coreuikit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kapmayn.core.analytics.AnalyticsTrackerHolder
import com.kapmayn.core.analytics.Events
import com.kapmayn.coreuikit.base.IContainer
import com.kapmayn.diproviders.provider.DiApp
import com.kapmayn.diproviders.provider.DiProvider

abstract class SimpleFragment : Fragment() {

    protected abstract val layoutId: Int
    protected open var needTransparentBars = false

    protected var bottomDialog: BottomSheetDialog? = null
    protected var messageDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject((activity?.application as DiApp).diProvider)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(layoutId, container, false)
        if (needTransparentBars) {
            rootView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        processArguments(arguments)
        setupViews(savedInstanceState)
        subscribe()
    }

    abstract fun inject(diProvider: DiProvider)

    override fun onStart() {
        super.onStart()
        AnalyticsTrackerHolder.getInstance().trackEvent("start ${this::class.java.simpleName}", Events.APP)
    }

    override fun onStop() {
        AnalyticsTrackerHolder.getInstance().trackEvent("stop ${this::class.java.simpleName}", Events.APP)
        bottomDialog?.cancel()
        messageDialog?.cancel()
        super.onStop()
    }

    protected open fun processArguments(bundle: Bundle?) {}

    protected open fun setupViews(savedInstanceState: Bundle?) {}

    protected open fun subscribe() {}

    protected fun getActivity(action: FragmentActivity.(FragmentActivity) -> Unit) {
        activity?.let { it.action(it) }
    }

    fun getContainer(action: IContainer.(IContainer) -> Unit) {
        val container = when {
            parentFragment is IContainer -> parentFragment as IContainer
            activity is IContainer -> activity as IContainer
            else -> null
        }

        container?.let { it.action(it) }
    }

    open fun onBackPressed(): Boolean {
        return false
    }

    open fun getSharedViews() = emptyList<View>()
}