package com.android.apps.views.fragments

import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * Created by annguyen on 1/30/18.
 */
abstract class BaseFragment : Fragment() {

    private var isFragmentRunning = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    protected open fun initialize() {
        // Init variable
        initialVariable()

        // Init view component
        initialViewComponent()

        load()
    }

    protected open fun load() {

    }

    protected open fun initialVariable() {

    }

    protected open fun initialViewComponent() {

    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    protected open fun getFragmentTitle(): String = ""

    protected open fun getFragmentIcon(): Int = 0

    open fun getFragmentId(): Int = 0

    override fun onResume() {
        super.onResume()
        isFragmentRunning = true
    }

    override fun onPause() {
        super.onPause()
        isFragmentRunning = false
    }

    fun isFragmentRunning(): Boolean {
        return isFragmentRunning
    }

    open fun onBackPressed() {

    }
}