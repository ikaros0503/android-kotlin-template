package com.android.apps.views.activities.main

import android.Manifest
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.android.apps.R
import com.android.apps.views.activities.BaseActivity
import timber.log.Timber

class MainActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_main

    override val enablePressAgainToExit: Boolean
        get() = true

    override fun initializeViewComponent() {
        super.initializeViewComponent()
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) {
            Timber.d("Grant permission result: $it")
        }
    }

    override fun onResume() {
        super.onResume()
    }
}
