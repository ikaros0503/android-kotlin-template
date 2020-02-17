package com.android.apps.components.viewpager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by annguyen on 1/30/18.
 */
class FragmentContentPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val listFragment = mutableListOf<Fragment>()

    override fun getItemCount(): Int = listFragment.size

    override fun createFragment(position: Int): Fragment = listFragment[position]

    fun addFragment(fragment: Fragment, index: Int = -1) {
        if (index >= 0) {
            listFragment.add(index, fragment)
        } else {
            listFragment.add(fragment)
        }
    }

    fun removeFragment(fragment: Fragment) {
        listFragment.remove(fragment)
    }

    fun removeFragment(index: Int) {
        listFragment.removeAt(index)
    }

    fun getFragment(position: Int) = listFragment[position]
}