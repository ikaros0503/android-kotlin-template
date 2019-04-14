package com.android.apps.extensions

typealias OnFragmentResults = (granted: List<String>, denied: List<String>) -> Unit

typealias OnPermissionDone = (permission: List<String>, all: Boolean) -> Unit