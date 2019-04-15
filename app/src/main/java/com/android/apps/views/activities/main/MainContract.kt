package com.android.apps.views.activities.main

import com.android.apps.api.model.event.Event
import com.android.apps.api.request.ApiServices
import com.android.apps.views.generic.BasePresenter
import com.android.apps.views.generic.BaseView
import io.reactivex.android.schedulers.AndroidSchedulers

interface MainContract {

    interface View : BaseView<Presenter> {

        fun appendEventToList(vararg event: Event)

    }

    interface Presenter: BasePresenter {
        fun fetchEvents(page: Int)
    }
}