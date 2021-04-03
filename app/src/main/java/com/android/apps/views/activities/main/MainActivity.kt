package com.android.apps.views.activities.main

import android.Manifest
import com.android.apps.R
import com.android.apps.databinding.ActivityMainBinding
import com.android.apps.views.activities.BaseActivity
import timber.log.Timber

class MainActivity : BaseActivity<ActivityMainBinding>({
    ActivityMainBinding.inflate(it)
}) {

    override val enablePressAgainToExit: Boolean
        get() = true

    override fun initializeViewComponent() {
        super.initializeViewComponent()
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) {
            Timber.d("Grant permission result: $it")
        }
    }
}
