package com.android.apps.utils.fragment

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

object FragmentUtils {

    private lateinit var mFragmentManager: FragmentManager
    private var mMainLayoutResId: Int = 0
    private var mInAnimation = android.R.anim.slide_in_left
    private var mOutAnimation = android.R.anim.slide_out_right

    fun init(fragmentManager: FragmentManager?, mainLayoutResId: Int) {
        this.mFragmentManager = fragmentManager!!
        this.mMainLayoutResId = mainLayoutResId
    }

    fun push(fragment: Fragment, withAnimation: Boolean = false, inAnimation: Int = -1, outAnimation: Int = -1) {
        val findFragment = mFragmentManager.findFragmentByTag(fragment.javaClass.simpleName)
        if (findFragment == null) {
            mFragmentManager.beginTransaction()
                    .let {
                        if (withAnimation && inAnimation == -1 && outAnimation == -1) {
                            return@let it.setCustomAnimations(mInAnimation, mOutAnimation)
                        } else if (withAnimation && inAnimation != -1 && outAnimation != -1) {
                            return@let it.setCustomAnimations(inAnimation, outAnimation)
                        }
                        return@let it
                    }
                    .add(mMainLayoutResId, fragment, fragment.javaClass.simpleName)
                    .addToBackStack(fragment.javaClass.simpleName)
                    .commitAllowingStateLoss()
        }
    }

    fun pop(withAnimation: Boolean = false, inAnimation: Int = -1, outAnimation: Int = -1): Boolean {
        if (::mFragmentManager.isInitialized.not())
            return false

        // If BackStack is empty, return false
        if (mFragmentManager.backStackEntryCount <= 0)
            return false

        val lastFragmentName = mFragmentManager.getBackStackEntryAt(mFragmentManager.backStackEntryCount - 1).name
        val lastFragment = mFragmentManager.findFragmentByTag(lastFragmentName)
        if (lastFragment != null) {
            mFragmentManager.beginTransaction()
                    .let {
                        if (withAnimation && inAnimation == -1 && outAnimation == -1) {
                            return@let it.setCustomAnimations(mInAnimation, mOutAnimation)
                        } else if (withAnimation && inAnimation != -1 && outAnimation != -1) {
                            return@let it.setCustomAnimations(inAnimation, outAnimation)
                        }
                        return@let it
                    }
                    .remove(lastFragment)
                    .commit()
            // Pop BackStack
            mFragmentManager.popBackStack()
            return true
        }

        // If last fragment not found, return false
        return false
    }

    fun get(tag: String): Fragment? {
        return mFragmentManager.findFragmentByTag(tag)
    }
}