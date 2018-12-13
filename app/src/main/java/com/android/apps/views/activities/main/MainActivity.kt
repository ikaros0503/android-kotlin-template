package com.android.apps.views.activities.main

import com.android.apps.R
import com.android.apps.views.activities.BaseActivity

class MainActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_main

    override val enablePressAgainToExit: Boolean
        get() = true

}
