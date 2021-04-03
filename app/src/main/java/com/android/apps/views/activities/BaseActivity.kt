package com.android.apps.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.android.apps.R
import com.android.apps.extensions.toast
import com.android.apps.utils.permission.RequestPermissionComponent
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

abstract class BaseActivity<V : ViewBinding>(
    private val bindingFactory: (LayoutInflater) -> V
) :
    AppCompatActivity() {

    private var _binding: V? = null

    protected val binding: V
        get() = _binding ?: throw IllegalAccessException("Binding wasn't inflated")

    private var disposableBackPressed: Disposable? = null

    private val requestPermissionComponent = RequestPermissionComponent()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionComponent.register(this)
        _binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
        initialize()
    }

    protected open fun initialize() {
        initializeVariable()
        initializeViewComponent()
    }

    fun navigateTo(activity: Class<*>, bundle: Bundle = Bundle()) {
        startActivity(Intent(this, activity), bundle)
    }

    fun requestPermission(
        permission: String,
        callback: RequestPermissionComponent.OnRequestPermissionCallBack
    ) {
        requestPermissionComponent.requestPermission(permission, callback)
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

    override fun onDestroy() {
        super.onDestroy()
        requestPermissionComponent.unregister()
    }
}