package com.android.apps.components.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import io.reactivex.disposables.Disposable

class DisposableLifecycleAware(instance: Disposable) : LifecycleAware<Disposable>(instance) {
    override fun close() {
        instance.dispose()
    }
}

fun LifecycleOwner.ownRx(disposable: Disposable) {
    if (this.lifecycle.currentState == Lifecycle.State.DESTROYED) {
        disposable.dispose()
        return
    }
    this.lifecycle.addObserver(DisposableLifecycleAware(disposable))
}

fun Disposable.attachLifecycle(lifecycleOwner: LifecycleOwner) {
    lifecycleOwner.ownRx(this)
}