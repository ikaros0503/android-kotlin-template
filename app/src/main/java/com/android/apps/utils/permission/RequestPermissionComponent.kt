package com.android.apps.utils.permission

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class RequestPermissionComponent : ActivityResultCallback<Boolean> {

    private var currentPermission: String = ""
    private var callback: OnRequestPermissionCallBack? = null

    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null

    fun register(activity: ComponentActivity) {
        requestPermissionLauncher = activity.registerForActivityResult(ActivityResultContracts.RequestPermission(), this)
    }

    fun requestPermission(permission: String, callback: OnRequestPermissionCallBack) {
        this.currentPermission = permission
        this.callback = callback
        requestPermissionLauncher?.launch(permission)
    }

    override fun onActivityResult(granted: Boolean) {
        if (currentPermission.isNotEmpty()) {
            callback?.onResult(granted)
        }
        callback = null
        currentPermission = ""
    }

    fun unregister() {
        requestPermissionLauncher?.unregister()
        requestPermissionLauncher = null
    }

    fun interface OnRequestPermissionCallBack {
        fun onResult(isGranted: Boolean)
    }
}



