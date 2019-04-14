package com.android.apps.views.activities.main

import android.util.Log
import com.android.apps.api.request.ApiServices
import com.android.apps.api.request.UIConfigServices
import io.reactivex.android.schedulers.AndroidSchedulers

class MainPresenter : MainConstract.Presenter {

    override fun start() {
        // TODO implement function
        fetchEvent()
    }

    private fun fetchEvent() {
       ApiServices.default.listEvents()
               .flatMapIterable { it }
               .observeOn(AndroidSchedulers.mainThread())
               .doOnNext {
                   Log.w("APICall", "Event=$it")
               }
               .subscribe()
    }
}