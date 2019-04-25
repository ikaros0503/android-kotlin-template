package com.android.apps.views.fragments.main

import android.Manifest
import com.android.apps.R
import com.android.apps.content.FBVideoContent
import com.android.apps.extensions.toast
import com.android.apps.utils.download.DownloadUtils
import com.android.apps.utils.permission.Permissions
import com.android.apps.views.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    private var currentVideo: FBVideoContent? = null

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initialViewComponent() {
        fab_download.hide()
        webview_home_fragment.resumeTimers()
        webview_home_fragment.registerOnVideoProcessed {
            currentVideo = it
            fab_download.show()
        }

        image_button_back.setOnClickListener {
            webview_home_fragment.goBack()
        }

        image_button_forward.setOnClickListener {
            webview_home_fragment.goForward()
        }

        toolbar_fragment_home.title = getString(R.string.app_name)

        image_button_refresh.setOnClickListener { webview_home_fragment.reload() }

        fab_download.setOnClickListener {
            Permissions.Builder(it.context)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .ensure()
                    .requestIfFailed()
                    .onGranted { _, all ->
                        if (all) {
                            download()
                            fab_download.hide()
                        }
                    }
                    .onDenied { _, all -> context?.toast("Our app need write to storage permission for work") }
                    .check()

        }
    }

    private fun download() {
        currentVideo?.let { video ->
            DownloadUtils.get(context!!).download(video.url)
        }
    }
}