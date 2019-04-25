package com.android.apps.extensions

import com.android.apps.content.FBVideoContent

typealias OnFragmentResults = (granted: List<String>, denied: List<String>) -> Unit

typealias OnPermissionDone = (permission: List<String>, all: Boolean) -> Unit

typealias OnFBVideoProcessed = (FBVideoContent) -> Unit

typealias OnWebviewURLChangeListener = (String) -> Unit