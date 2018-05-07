package com.android.apps.utils.rx

import android.util.SparseArray
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

object Rx {

    private val mListObservable = SparseArray<BehaviorSubject<*>>()

    @Suppress("UNCHECKED_CAST")
    fun <T> subscribe(uniqueNumber: Int, action: (T) -> Unit, isResetSubscribe: Boolean = false): Disposable {
        var emitter = mListObservable[uniqueNumber]
        if (emitter == null || isResetSubscribe) {
            emitter = BehaviorSubject.create<T>()
            mListObservable.put(uniqueNumber, emitter)
        }
        return emitter.subscribe { action.invoke(it as T) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> notify(uniqueNumber: Int, value: T) {
        val emitter = mListObservable[uniqueNumber] as BehaviorSubject<T>?
        emitter?.onNext(value)
    }
}