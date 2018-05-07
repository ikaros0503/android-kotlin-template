package com.android.apps.utils.permission

import android.Manifest
import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import io.reactivex.Emitter
import io.reactivex.Observable


class RxPermission {

    private var mCheckDrawOverApps = false
    private lateinit var mObservable: Observable<Map<String, Boolean>>
    private var mContext: Context? = null
    private lateinit var mPermission: MutableMap<String, Boolean>
    private var mRxPermissionFragment: RxPermissionFragment? = null

    constructor(context: Context) {
        mContext = context
    }

    fun request(vararg permissions: String): RxPermission {
        if (mContext !is Activity) {
            throw IllegalStateException("Activity has not been set.")
        }
        mPermission = mutableMapOf(*permissions.map { Pair(it, false) }.toTypedArray())
        init(permissions)
        return this
    }

    private fun init(permissions: Array<out String>) {
        mObservable = Observable.create { emitter ->
            var results = mapOf(*permissions.map { permission ->
                val result = ActivityCompat.checkSelfPermission(mContext!!, permission) == PackageManager.PERMISSION_GRANTED
                Pair(permission, result)
            }.toTypedArray())

            if (mCheckDrawOverApps) {
                results = results.plus(Pair(Manifest.permission.SYSTEM_ALERT_WINDOW, RxPermission.canDrawOverApps(mContext!!)))
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                emitter.onNext(results)
                emitter.onComplete()
            } else {
                if (mRxPermissionFragment == null) {
                    mRxPermissionFragment = RxPermissionFragment.create(emitter)
                }
                (mContext as Activity).fragmentManager.beginTransaction().add(mRxPermissionFragment, TAG).commitAllowingStateLoss()
                (mContext as Activity).fragmentManager.executePendingTransactions()

                mRxPermissionFragment?.requestPermission(*results.map { it.key }.toTypedArray())
            }
        }
        mObservable.doOnComplete {
            mRxPermissionFragment = null
        }
    }

    fun ensure(vararg permissions: String): RxPermission {
        mObservable = Observable.create { emitter ->
            mPermission = mutableMapOf(*permissions.map { permission ->
                val result = ActivityCompat.checkSelfPermission(
                        mContext!!, permission) == PackageManager.PERMISSION_GRANTED
                Pair(permission, result)
            }.toTypedArray())
            if (mCheckDrawOverApps) {
                mPermission[Manifest.permission.SYSTEM_ALERT_WINDOW] = RxPermission.canDrawOverApps(mContext!!)
            }
            emitter.onNext(mPermission)
            emitter.onComplete()
        }
        return this
    }

    fun successThen(action: () -> Unit): RxPermission {
        mObservable = mObservable.doOnNext {
            if (isGrantedAll(it))
                action.invoke()
        }
        return this
    }

    fun failedThen(action: () -> Unit): RxPermission {
        mObservable = mObservable.doOnNext {
            if (!isGrantedAll(it))
                action.invoke()
        }
        return this
    }

    fun <T> successThen(observable: Observable<T>): RxPermission {
        mObservable = mObservable.doOnNext {
            if (isGrantedAll(it))
                observable.subscribe()
        }
        return this
    }

    fun <T> failedThen(observable: Observable<T>): RxPermission {
        mObservable = mObservable.doOnNext {
            if (!isGrantedAll(it)) {
                observable.subscribe()
            }
        }
        return this
    }

    fun onDone(action: (Map<String, Boolean>) -> Unit): RxPermission {
        mObservable = mObservable.doOnNext(action)
        return this
    }

    fun go() {
        mObservable.subscribe()
    }

    fun checkDrawOverApps(): RxPermission {
        mCheckDrawOverApps = true
        return this
    }

    companion object {

        const val REQUEST_PERMISSION_CODE_NUMBER = 908
        const val REQUEST_DRAW_OVER_APP_PERMISSION_CODE_NUMBER = 90190
        const val TAG = "RxPermissions"

        fun from(context: Context): RxPermission {
            return RxPermission(context)
        }

        fun canDrawOverApps(context: Context): Boolean {
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) true else Settings.canDrawOverlays(context)
        }

        fun isGrantedAll(permissions: Map<String, Boolean>): Boolean {
            return permissions.let {
                it.forEach { permission ->
                    if (!permission.value) {
                        return@let false
                    }
                }
                true
            }
        }
    }
}

class RxPermissionFragment : Fragment() {

    companion object {
        fun create(emitter: Emitter<Map<String, Boolean>>): RxPermissionFragment {
            return RxPermissionFragment().apply {
                mEmitter = emitter
            }
        }

        const val REQUEST_PERMISSION_CODE_NUMBER = 991

        const val REQUEST_DRAW_OVER_APP_PERMISSION_CODE_NUMBER = 992

    }

    private lateinit var mEmitter: Emitter<Map<String, Boolean>>
    private lateinit var mListPermission: MutableMap<String, Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun requestPermission(vararg permissions: String) {
        val ungrantedPermission = checkAndMapPermission(permissions)
        if (ungrantedPermission.isEmpty()) {
            return done()
        }
        return requestPermissions(ungrantedPermission, REQUEST_PERMISSION_CODE_NUMBER)
    }

    private fun done() {
        mEmitter.onNext(mListPermission)
        mEmitter.onComplete()
    }

    private fun checkAndMapPermission(permissions: Array<out String>): Array<String> {
        val ungrantedPermission = arrayListOf<String>()
        mListPermission = mutableMapOf(
                *permissions.map { permission ->
                    val isGranted = ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
                    if (!isGranted) {
                        ungrantedPermission.add(permission)
                    }
                    Pair(permission, isGranted)
                }.toTypedArray()
        )
        return ungrantedPermission.toTypedArray()
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun requestDrawOverApps() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + context.packageName))
        startActivityForResult(intent, REQUEST_DRAW_OVER_APP_PERMISSION_CODE_NUMBER)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        if (requestCode == REQUEST_PERMISSION_CODE_NUMBER) {
            permissions?.forEachIndexed { index, permission ->
                mListPermission[permission] = grantResults!![index] == PackageManager.PERMISSION_GRANTED
            }
            if (permissions?.contains(Manifest.permission.SYSTEM_ALERT_WINDOW)!! && !RxPermission.canDrawOverApps(activity)) {
                requestDrawOverApps()
            } else {
                done()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_DRAW_OVER_APP_PERMISSION_CODE_NUMBER) {
            mListPermission[Manifest.permission.SYSTEM_ALERT_WINDOW] = RxPermission.canDrawOverApps(activity)
            done()
        }
    }
}