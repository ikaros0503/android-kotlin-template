package com.android.apps.extensions

import com.android.apps.api.model.event.Event

typealias OnFragmentResults = (granted: List<String>, denied: List<String>) -> Unit

typealias OnPermissionDone = (permission: List<String>, all: Boolean) -> Unit

typealias OnEventItemClickListener = (Event, Int) -> Unit