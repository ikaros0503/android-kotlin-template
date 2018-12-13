package com.android.apps.components.view.layout.collapsingtoolbar

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.util.AttributeSet
import android.view.View
import com.android.apps.R

class CollapsingToolbarWrapper : AppBarLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        View.inflate(context, R.layout.layout_collapsing_toolbar, this)
        initialize()
    }

    private fun initialize() {

    }
}