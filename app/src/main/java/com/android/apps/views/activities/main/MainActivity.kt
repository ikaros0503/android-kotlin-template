package com.android.apps.views.activities.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.android.apps.R
import com.android.apps.data.user.User
import com.android.apps.databinding.ActivityMainBinding
import com.android.apps.views.activities.BaseActivity

class MainActivity : BaseActivity(), MainConstract.View {

    override val presenter: MainConstract.Presenter = MainPresenter()

    override fun getLayoutId(): Int = R.layout.activity_main

    override val enablePressAgainToExit: Boolean
        get() = true

    override fun initializeViewComponent() {
        super.initializeViewComponent()
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }
}
