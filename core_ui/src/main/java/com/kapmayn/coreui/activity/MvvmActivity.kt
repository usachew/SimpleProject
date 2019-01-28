package com.kapmayn.coreui.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.kapmayn.core.base.Router
import javax.inject.Inject

abstract class MvvmActivity<ROUTER : Router> : SimpleActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var router: ROUTER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribe()
    }

    protected open fun subscribe() {}
}