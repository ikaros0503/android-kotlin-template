package com.android.apps.views.activities.main

import com.android.apps.R
import com.android.apps.api.model.event.Event
import com.android.apps.api.request.ApiServices
import com.android.apps.components.content.video.VideoAdapter
import com.android.apps.components.recyclerview.EndlessRecyclerViewScrollListener
import com.android.apps.views.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainContract.View {

    override val presenter: MainContract.Presenter by lazy {
        MainPresenter(ApiServices.default, this)
    }

    private val eventAdapter = VideoAdapter()

    override fun getLayoutId(): Int = R.layout.activity_main

    override val enablePressAgainToExit: Boolean
        get() = true

    override fun initializeViewComponent() {
        recyclerview_video_item.adapter = eventAdapter
        recyclerview_video_item.addOnScrollListener(object : EndlessRecyclerViewScrollListener(recyclerview_video_item.layoutManager!!) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                presenter.fetchEvents(page)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun appendEventToList(vararg event: Event) {
        eventAdapter.add(*event)
    }
}
