package com.android.apps.extensions

import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView

fun BottomNavigationView.connectWithViewPager(viewPager: ViewPager) {
    setOnNavigationItemSelectedListener {
        for (index in 0 until menu.size()) {
            if (menu.getItem(index).itemId == it.itemId) {
                viewPager.currentItem = index
                break
            }
        }
        true
    }
}

fun BottomNavigationView.getBehavior(): CoordinatorLayout.Behavior<*>? {
    return (layoutParams as CoordinatorLayout.LayoutParams).let {
        it.behavior
    }
}