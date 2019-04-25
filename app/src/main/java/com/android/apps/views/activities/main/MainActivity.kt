package com.android.apps.views.activities.main

import android.webkit.WebChromeClient
import androidx.viewpager.widget.ViewPager
import com.android.apps.R
import com.android.apps.components.behavior.BottomNavigationBehavior
import com.android.apps.components.viewpager.adapter.FragmentContentPagerAdapter
import com.android.apps.content.FBVideoContent
import com.android.apps.extensions.connectWithViewPager
import com.android.apps.extensions.getBehavior
import com.android.apps.utils.notification.NotificationCenter
import com.android.apps.utils.permission.Permissions
import com.android.apps.views.activities.BaseActivity
import com.android.apps.views.fragments.main.DownloadFragment
import com.android.apps.views.fragments.main.HomeFragment
import com.android.apps.views.fragments.main.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class MainActivity : BaseActivity() {

    private lateinit var viewPagerAdapter: FragmentContentPagerAdapter


    override fun getLayoutId(): Int = R.layout.activity_main

    override val enablePressAgainToExit: Boolean
        get() = true

    override fun initializeVariable() {
        viewPagerAdapter = FragmentContentPagerAdapter(supportFragmentManager,
                HomeFragment(),
                DownloadFragment(),
                SettingsFragment()
        )

        initializeNotification()
    }

    override fun initializeViewComponent() {
        viewpager_main_activity.adapter = viewPagerAdapter
        viewpager_main_activity.offscreenPageLimit = viewPagerAdapter.count

        bottom_nav_main_activity.connectWithViewPager(viewpager_main_activity)
        viewpager_main_activity.postDelayed({
            setupWebViewListener()
        }, 100)
        viewpager_main_activity.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                // TODO implement function
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // TODO implement function
            }

            override fun onPageSelected(position: Int) {
                (bottom_nav_main_activity.getBehavior() as? BottomNavigationBehavior)?.setBehaviorEnable(position == 0)
            }
        })
    }

    private fun setupWebViewListener() {

    }

    private fun initializeNotification() {

    }
}
