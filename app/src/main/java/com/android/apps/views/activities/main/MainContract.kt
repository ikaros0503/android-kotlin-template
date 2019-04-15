package com.android.apps.views.activities.main

import com.android.apps.views.generic.BasePresenter
import com.android.apps.views.generic.BaseView

interface MainContract {

    interface View : BaseView<Presenter> {

    }

    interface Presenter: BasePresenter {

    }
}