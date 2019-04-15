package com.android.apps.views.activities.main

import com.android.apps.R
import com.android.apps.api.request.ApiServices
import com.android.apps.views.activities.BaseActivity

class MainActivity : BaseActivity(), MainContract.View {

    override val presenter: MainContract.Presenter by lazy {
        MainPresenter(ApiServices.default, this)
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override val enablePressAgainToExit: Boolean
        get() = true

    override fun onResume() {
        super.onResume()
        presenter.start()
    }
}
