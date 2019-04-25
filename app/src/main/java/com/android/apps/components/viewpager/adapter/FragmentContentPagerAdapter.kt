package com.android.apps.components.viewpager.adapter

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.android.apps.views.fragments.BaseFragment

/**
 * Created by annguyen on 1/30/18.
 */
class FragmentContentPagerAdapter(fm: FragmentManager, vararg fragments: BaseFragment) : FragmentStatePagerAdapter(fm) {

    private val listFragment = arrayListOf(*fragments)


    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    override fun getCount(): Int {
        return listFragment.size
    }

    @Suppress("CAST_NEVER_SUCCEEDS")
    override fun getPageTitle(position: Int): CharSequence? {
        return try { listFragment[position].getFragmentTitle() } catch (e: ArrayIndexOutOfBoundsException) { "" }
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {

    }

    fun addFragment(fragment: BaseFragment) {
        listFragment.add(fragment)
    }

    fun addFragment(fragment: BaseFragment, index: Int) {
        listFragment.add(index, fragment)
    }

    fun removeFragment(fragment: BaseFragment) {
        listFragment.remove(fragment)
    }

    fun removeFragment(index: Int) {
        listFragment.removeAt(index)
    }

    fun getItemPosition(fragmentId: Int) : Int {
        return listFragment.indexOfFirst { it.getFragmentId() == fragmentId }
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

}