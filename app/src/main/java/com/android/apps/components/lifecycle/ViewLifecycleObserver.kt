package com.android.apps.components.lifecycle

import android.content.Context
import android.view.View
import androidx.lifecycle.*

interface ViewLifecycleObserver: LifecycleOwner  {
    val lifecycleRegistry: LifecycleRegistry
}