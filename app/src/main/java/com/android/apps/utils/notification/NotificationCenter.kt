package com.android.apps.utils.notification

import android.util.SparseArray
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

class NotificationCenter {

    private val observers = SparseArray<PublishSubject<*>>()

    @Suppress("UNCHECKED_CAST")
    fun <T> add(key: Int, target: (T) -> Unit): Disposable {
        val emitter = observers[key] ?: PublishSubject.create<T>().also { observers.put(key, it) }
        return emitter.subscribe { target.invoke(it as T) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> notify(key: Int, value: T) {
        (observers[key] as? PublishSubject<T>)?.onNext(value)
    }


    companion object {
        private var instance: NotificationCenter? = null

        val default: NotificationCenter
            get() {
                return instance ?: synchronized(NotificationCenter::class.java) {
                    instance ?: NotificationCenter()
                }
            }
    }
}