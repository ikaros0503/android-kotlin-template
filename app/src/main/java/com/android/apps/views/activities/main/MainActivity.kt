package com.android.apps.views.activities.main

import androidx.viewpager2.widget.ViewPager2
import com.android.apps.R
import com.android.apps.components.viewpager.adapter.FragmentContentPagerAdapter
import com.android.apps.extensions.SimpleOnTabSelectedListener
import com.android.apps.utils.permission.Permissions
import com.android.apps.views.activities.BaseActivity
import com.android.apps.views.fragments.BaseFragment
import com.android.apps.views.fragments.main.SettingFragment
import com.android.apps.views.fragments.main.WebFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_main

    private lateinit var pagerAdapter: FragmentContentPagerAdapter

    override val enablePressAgainToExit: Boolean
        get() = true

    override fun initializeVariable() {
        pagerAdapter = FragmentContentPagerAdapter(supportFragmentManager, lifecycle).apply {
            addFragment(WebFragment())
            addFragment(SettingFragment())
        }
    }

    override fun initializeViewComponent() {
        with(view_pager_main) {
            adapter = pagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    tab_layout_main.selectTab(tab_layout_main.getTabAt(position))
                }
            })

            isUserInputEnabled = false
        }
        with(tab_layout_main) {
            addTab(newTab().setText("Trực tiếp"))
            addTab(newTab().setText("Cài đặt"))

            addOnTabSelectedListener(object : SimpleOnTabSelectedListener() {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    view_pager_main.currentItem = tab.position
                }
            })
        }
    }

    override fun onBackPressed() {
        if (view_pager_main.currentItem == 0) {
            val fragment = pagerAdapter.getFragment(0) as WebFragment
            if (fragment.canGoBack()) {
                fragment.onBackPressed()
            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }
}
