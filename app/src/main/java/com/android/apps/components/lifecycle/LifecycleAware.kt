package com.android.apps.components.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

abstract class LifecycleAware<T>(protected val instance: T): LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun closeInstance() {
        close()
    }

    abstract fun close()
}