package com.android.apps.utils.permission

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.android.apps.BuildConfig
import com.android.apps.extensions.OnFragmentResults
import com.android.apps.extensions.OnPermissionDone
import com.android.apps.extensions.isMarshmallowAbove
import com.android.apps.extensions.isOreoAbove
import com.android.apps.utils.bundle.get
import com.android.apps.utils.bundle.init
import com.android.apps.utils.fragment.get
import com.android.apps.utils.fragment.remove

class Permissions private constructor(builder: Builder) {

    private val context: Context = builder.context
    private val permissions = builder.permissions
    private val isEnsure = builder.isEnsure
    private val isRequestIfFailed = builder.isRequestIfFailed
    private val isCheckDrawOverApps = builder.isCheckDrawOverApps
    private val onGranted: OnPermissionDone? = builder.onGranted
    private val onDenied: OnPermissionDone? = builder.onDenied

    /**
     * Call check
     */
    internal fun check() {
        // If Android device versions is below M, then return granted
        val permission = if (isCheckDrawOverApps) listOf(android.Manifest.permission.SYSTEM_ALERT_WINDOW) else permissions
        if (!isMarshmallowAbove()) {
            onGranted?.invoke(permission, true)
        } else {
            PermissionFragment.startFragmentForResults(context, permission, isEnsure, isRequestIfFailed) { granted, denied ->
                granted.takeIf { it.isNotEmpty() }?.also {
                    onGranted?.invoke(it, it.size == permission.size)
                }
                denied.takeIf { it.isNotEmpty() }?.also {
                    onDenied?.invoke(it, it.size == permission.size)
                }
                (context as FragmentActivity).supportFragmentManager.apply {
                    get(PermissionFragment::class.java.simpleName)?.let {
                        remove(it)
                    }
                }
            }
        }
    }

    /**
     * Retained fragment use for checking app permission
     * This will be remove after checked
     */
    class PermissionFragment : Fragment() {

        private var permissions: List<String> = listOf()
        private var onFragmentResult: OnFragmentResults? = null
        private var isEnsure: Boolean = true
        private var isRequestIfFailed: Boolean = false

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            retainInstance = true

            permissions = arguments?.get(ARGS_KEY_PERMISSIONS, arrayOf<String>())?.toList()
                ?: permissions
            checkAndRequestPermission()
        }

        /**
         * Call check and request permission
         */
        private fun checkAndRequestPermission() {
            if (permissions.contains(android.Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                if (!checkDrawOverApp()) {
                    if (isRequestIfFailed) {
                        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + BuildConfig.APPLICATION_ID))
                        startActivityForResult(intent, PERMISSION_REQUEST_CODE)
                    } else {
                        onFragmentResult?.invoke(emptyList(), permissions)
                    }
                } else {
                    onFragmentResult?.invoke(permissions, emptyList())
                }
            } else {
                if (isRequestIfFailed) {
                    requestPermissions(permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
                } else {
                    val granted = permissions.filter {
                        ActivityCompat.checkSelfPermission(context!!, it) == PackageManager.PERMISSION_GRANTED
                    }
                    val denied = permissions.minus(granted)
                    onFragmentResult?.invoke(granted, denied)
                }
            }
        }

        /**
         * Check is app allow draw over apps
         * Return true if allowed, otherwise false
         */
        private fun checkDrawOverApp(): Boolean {
            val view = View(context)
            val windowManager = context?.getSystemService(WINDOW_SERVICE) as? WindowManager
            val layoutParams = WindowManager.LayoutParams().apply {
                @Suppress("DEPRECATION")
                type = if (isOreoAbove()) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            }
            return try {
                windowManager?.addView(view, layoutParams)
                windowManager?.removeView(view)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        /**
         * Handle on DrawOverApps permission result
         * with [requestCode], [resultCode], and [data]
         */
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == PERMISSION_REQUEST_CODE) {
                if (checkDrawOverApp()) {
                    onFragmentResult?.invoke(permissions, emptyList())
                } else {
                    onFragmentResult?.invoke(emptyList(), permissions)
                }
            }
        }

        /**
         * Handle on request normal permission result
         * Return result of [requestCode] and [permissions], return [grantResults]
         */
        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            if (requestCode == PERMISSION_REQUEST_CODE) {
                val granted = permissions.filterIndexed { index, _ ->
                    grantResults[index] == PackageManager.PERMISSION_GRANTED
                }
                val denied = permissions.asList().minus(granted)
                onFragmentResult?.invoke(granted, denied)
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            Log.w("FragmentPermission", "Destroyed")
        }

        companion object {
            /**
             * Constant for permission passed to arguments
             */
            private const val ARGS_KEY_PERMISSIONS = "com.android.apps.utils.permission.Permission.PermissionFragment.listPermissions"
            /**
             * Constant for permission request code
             */
            private const val PERMISSION_REQUEST_CODE = 991

            /**
             * Get new instance of fragment with [permission]
             */
            fun newInstance(permission: List<String>): PermissionFragment {
                return PermissionFragment().apply {
                    arguments = Bundle().init(ARGS_KEY_PERMISSIONS to permission.toTypedArray())
                }
            }

            /**
             * Start PermissionFragment for result with [permission], then response to [onFragmentResult]
             */
            fun startFragmentForResults(context: Context, permission: List<String>, ensure: Boolean, requestIfFailed: Boolean, onFragmentResult: OnFragmentResults) {
                if (context !is FragmentActivity) {
                    throw IllegalArgumentException("Your context is not an instance of Activity")
                }
                val fragment = newInstance(permission).apply {
                    this.onFragmentResult = onFragmentResult
                    this.isEnsure = ensure
                    this.isRequestIfFailed = requestIfFailed
                }
                context.supportFragmentManager.apply {
                    beginTransaction().add(fragment, fragment::class.java.simpleName).commitAllowingStateLoss()
                    executePendingTransactions()
                }
            }
        }
    }


    class Builder(internal val context: Context) {

        internal var permissions: MutableList<String> = mutableListOf()
        internal var isEnsure: Boolean = false
        internal var isRequestIfFailed: Boolean = false
        internal var isCheckDrawOverApps: Boolean = false
        internal var onGranted: OnPermissionDone? = null
        internal var onDenied: OnPermissionDone? = null

        /**
         * Ensure set permissions has been granted
         */
        fun ensure(): Builder {
            isEnsure = true
            return this
        }

        /**
         * Prompt request permission if failed
         */
        fun requestIfFailed(): Builder {
            isRequestIfFailed = true
            return this
        }

        /**
         * Set [permissions]
         */
        fun permission(vararg permissions: String): Builder {
            this.permissions.addAll(permissions)
            return this
        }

        /**
         * Check if apps is allow draw over app
         * Note: If check this permission, others permissions will be ignores
         */
        fun checkDrawOverApps(): Builder {
            isCheckDrawOverApps = true
            return this
        }

        /**
         * Register [action] when permission granted
         */
        fun onGranted(action: OnPermissionDone): Builder {
            this.onGranted = action
            return this
        }

        /**
         * Register [action] when permission denied
         */
        fun onDenied(action: OnPermissionDone): Builder {
            this.onDenied = action
            return this
        }

        /**
         * Build
         */
        fun build(): Permissions {
            return Permissions(this)
        }

        /**
         * Call build and check permission
         */
        fun check() {
            Permissions(this).check()
        }
    }

    interface OnPermissionRequestDone {
        fun onDone(permission: List<String>, all: Boolean)
    }
}