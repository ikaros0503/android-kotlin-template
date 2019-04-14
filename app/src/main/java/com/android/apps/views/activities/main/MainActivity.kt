package com.android.apps.views.activities.main

import com.android.apps.R
import com.android.apps.utils.permission.Permissions
import com.android.apps.views.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_main

    override val enablePressAgainToExit: Boolean
        get() = true

    override fun initializeViewComponent() {
        super.initializeViewComponent()
        button.setOnClickListener {
            Permissions.Builder(this)
                    .checkDrawOverApps()
                    .ensure()
                    .requestIfFailed()
                    .onGranted { permission, all ->

                    }
                    .onDenied { permission, all ->

                    }
                    .check()
        }
    }
}
