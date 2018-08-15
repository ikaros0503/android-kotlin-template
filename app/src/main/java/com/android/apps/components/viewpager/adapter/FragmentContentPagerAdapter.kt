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
class FragmentContentPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    private val mListFragment: ArrayList<Fragment> = ArrayList()


    override fun getItem(position: Int): Fragment {
        return mListFragment[position]
    }

    override fun getCount(): Int {
        return mListFragment.size
    }

    @Suppress("CAST_NEVER_SUCCEEDS")
    override fun getPageTitle(position: Int): CharSequence? {
        return ""
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {

    }

    fun addFragment(fragment: Fragment) {
        mListFragment.add(fragment)
    }

    fun addFragment(fragment: Fragment, index: Int) {
        mListFragment.add(index, fragment)
    }

    fun removeFragment(fragment: Fragment) {
        mListFragment.remove(fragment)
    }

    fun removeFragment(index: Int) {
        mListFragment.removeAt(index)
    }

    fun getItemPosition(fragmentId: Int) : Int {
        return mListFragment.indexOfFirst { (it as BaseFragment).getFragmentId() == fragmentId }
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