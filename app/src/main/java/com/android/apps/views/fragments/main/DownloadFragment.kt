package com.android.apps.views.fragments.main

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.apps.R
import com.android.apps.components.recyclerview.adapter.VideoItemAdapter
import com.android.apps.content.FBVideoContent
import com.android.apps.utils.download.DownloadUtils
import com.android.apps.utils.permission.Permissions
import com.android.apps.views.fragments.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_download.*

class DownloadFragment : BaseFragment() {

    private lateinit var itemAdapter: VideoItemAdapter

    override fun getLayoutId(): Int = R.layout.fragment_download

    override fun initialize() {
        initialVariable()
        initialViewComponent()
    }

    override fun initialVariable() {
        itemAdapter = VideoItemAdapter()
    }

    override fun initialViewComponent() {
        with(recyclerview_list_items) {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setItemViewCacheSize(50)
        }
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            load()
        } else {
            showErrorLayout()
        }

        swipe_refresh_layout_fragment_download.setOnRefreshListener {
            itemAdapter.clear()
            load()
        }
    }

    private fun checkPermission() {
        Permissions.Builder(context!!)
                .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .ensure()
                .onGranted { _, all ->
                    load()
                }
                .onDenied { _, all ->
                    showErrorLayout()
                }
                .check()
    }

    override fun load() {
        DownloadUtils.get(context!!)
                .fetchFileFromDownloadFolder()
                .flatMapIterable { it }
                .doOnNext { itemAdapter.addContent(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progress_layout_fragment_download.showLoading() }
                .doOnComplete {
                    itemAdapter.notifyDataSetChanged()
                    if (itemAdapter.itemCount <= 0) {
                        showEmptyLayout()
                    } else {
                        progress_layout_fragment_download.showContent()
                    }
                    swipe_refresh_layout_fragment_download.isRefreshing = false
                }
                .subscribe()
    }

    private fun showErrorLayout() {
        progress_layout_fragment_download.showError(
                R.drawable.ic_warning_grey_900_48dp,
                "Permission needed",
                "Our apps need Read Storage permission, please check and granted it",
                "Grant permission"
        ) {
            checkPermission()
        }
    }

    private fun showEmptyLayout() {
        progress_layout_fragment_download.showEmpty(R.drawable.ic_inbox_grey_900_48dp, "No downloaded video", "Let's go to Facebook and download something!!!")
    }
}