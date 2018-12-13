package com.android.apps.utils.rx

import android.util.SparseArray
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

object Rx {

    private val listObservable = SparseArray<PublishSubject<*>>()

    @Suppress("UNCHECKED_CAST")
    fun <T> subscribe(uniqueNumber: Int, action: (T) -> Unit, isResetSubscribe: Boolean = false): Disposable {
        var emitter = listObservable[uniqueNumber]
        if (emitter == null || isResetSubscribe) {
            emitter = PublishSubject.create<T>()
            listObservable.put(uniqueNumber, emitter)
        }
        return emitter.subscribe { action.invoke(it as T) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> notify(uniqueNumber: Int, value: T) {
        val emitter = listObservable[uniqueNumber] as PublishSubject<T>?
        emitter?.onNext(value)
    }
}