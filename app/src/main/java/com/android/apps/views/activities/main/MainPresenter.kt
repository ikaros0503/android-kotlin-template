package com.android.apps.views.activities.main

import android.util.Log
import com.android.apps.api.request.ApiServices
import io.reactivex.android.schedulers.AndroidSchedulers

class MainPresenter(private val api: ApiServices,
                    val view: MainContract.View) : MainContract.Presenter {

    override fun start() {
        // TODO implement function
    }
}