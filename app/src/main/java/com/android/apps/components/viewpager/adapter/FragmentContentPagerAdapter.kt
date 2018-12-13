package com.android.apps.components.viewpager.adapter

import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.android.apps.views.fragments.BaseFragment

/**
 * Created by annguyen on 1/30/18.
 */
class FragmentContentPagerAdapter(fm: FragmentManager?, defaultFragments: Array<BaseFragment>? = null) : FragmentStatePagerAdapter(fm) {

    private val mListFragment = defaultFragments?.let { arrayListOf(*it) } ?: arrayListOf()


    override fun getItem(position: Int): Fragment {
        return mListFragment[position]
    }

    override fun getCount(): Int {
        return mListFragment.size
    }

    @Suppress("CAST_NEVER_SUCCEEDS")
    override fun getPageTitle(position: Int): CharSequence? {
        return try { mListFragment[position].getFragmentTitle() } catch (e: ArrayIndexOutOfBoundsException) { "" }
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {

    }

    fun addFragment(fragment: BaseFragment) {
        mListFragment.add(fragment)
    }

    fun addFragment(fragment: BaseFragment, index: Int) {
        mListFragment.add(index, fragment)
    }

    fun removeFragment(fragment: BaseFragment) {
        mListFragment.remove(fragment)
    }

    fun removeFragment(index: Int) {
        mListFragment.removeAt(index)
    }

    fun getItemPosition(fragmentId: Int) : Int {
        return mListFragment.indexOfFirst { it.getFragmentId() == fragmentId }
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    companion object {
        fun init(fragmentManager: FragmentManager?): FragmentContentPagerAdapter {
            return FragmentContentPagerAdapter(fragmentManager)
//                    .apply {
//                        addFragment(HomeFragment())
//                        addFragment(PlaylistFragment())
//                        addFragment(SearchFragment())
//                        addFragment(AccountFragment())
//                    }
        }
    }
}