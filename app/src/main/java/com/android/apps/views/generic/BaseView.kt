package com.android.apps.views.generic

interface BaseView<T: BasePresenter> {
    val presenter: T
}