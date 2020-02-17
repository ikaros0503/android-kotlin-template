package com.android.apps.views.activities.splash

import android.content.Intent
import android.os.Build
import android.view.View
import com.android.apps.R
import com.android.apps.components.lifecycle.ownRx
import com.android.apps.components.view.captcha.CaptchaView
import com.android.apps.repository.uiconfig.UIConfigRepository
import com.android.apps.utils.networkAvailable
import com.android.apps.utils.prefs.Preferences
import com.android.apps.utils.prefs.put
import com.android.apps.views.activities.BaseActivity
import com.android.apps.views.activities.main.MainActivity
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SplashActivity : BaseActivity() {

    private val prefs by lazy { Preferences.default }

    override fun getLayoutId(): Int = R.layout.activity_splash

    override fun initializeViewComponent() {
        setActivityFullScreen()
        load()
    }

    private fun load() {
        ownRx(Flowable.fromCallable(::networkAvailable)
            .subscribeOn(Schedulers.io())
            .flatMap(this::createLoadingRx)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ doAfterFetchData() }) {
                it.printStackTrace()
                doAfterFetchData()
            })
    }

    private fun createLoadingRx(networkAvailable: Boolean) =
        UIConfigRepository.instance.fetchConfig(this, networkAvailable)


    private fun doAfterFetchData() {
        if (prefs.contains(KEY_APP_OPENED).not()) {
            showCaptchaView()
        } else {
            startMainActivity()
        }
    }

    private fun showCaptchaView() {
        val captchaView = CaptchaView(this)
        setContentView(captchaView)
        captchaView.registerOnSuccessListener {
            startMainActivity()
            prefs.put(KEY_APP_OPENED, true)
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setActivityFullScreen() {
        var flag: Int = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flag = flag or View.SYSTEM_UI_FLAG_IMMERSIVE
        }

        window.decorView.systemUiVisibility = flag
    }

    companion object {
        private const val PREFIX = "com.android.apps.views.activities.splash"
        private const val KEY_APP_OPENED = "$PREFIX.KEY_APP_OPENED"
    }
}