package com.android.apps.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.apps.R
import com.android.apps.extensions.toast
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable

import java.util.concurrent.TimeUnit

abstract class BaseActivity: AppCompatActivity() {

    private var disposableBackPressed: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initialize()
    }

    abstract fun getLayoutId(): Int

    protected open fun initialize() {
        initializeVariable()
        initializeViewComponent()
    }

    fun navigateTo(activity: Class<*>, bundle: Bundle = Bundle()) {
        startActivity(Intent(this, activity), bundle)
    }

    protected open fun initializeVariable() {}

    protected open fun initializeViewComponent() {}

    protected open val enablePressAgainToExit: Boolean = false

    override fun onBackPressed() {
        if (!enablePressAgainToExit)
            return super.onBackPressed()

        disposableBackPressed?.takeIf { it.isDisposed.not() }?.let {
            it.dispose()
            super.onBackPressed()
        } ?: run {
            toast(messageId = R.string.text_press_back_again_to_exit)
            disposableBackPressed = Observable.timer(2000, TimeUnit.MILLISECONDS).subscribe()
        }
    }
}