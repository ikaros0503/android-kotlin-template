package com.android.apps.views.activities.main

import com.android.apps.api.request.ApiServices
import com.android.apps.api.request.ApiServices.Companion.DEFAULT_MAX_ITEM_FETCHED
import io.reactivex.android.schedulers.AndroidSchedulers

class MainPresenter(private val api: ApiServices,
                    val view: MainContract.View) : MainContract.Presenter {


    override fun start() {
        fetchEvents(0)
    }

    override fun fetchEvents(page: Int) {
        api.listEvents(skip = page * DEFAULT_MAX_ITEM_FETCHED)
                .map { it.toTypedArray() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { view.appendEventToList(*it) }
                .subscribe()
    }
}