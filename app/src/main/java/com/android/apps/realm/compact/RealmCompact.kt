package com.android.apps.realm.compact

import io.realm.CompactOnLaunchCallback

class RealmCompactOnLaunchCallback : CompactOnLaunchCallback {
    override fun shouldCompact(totalBytes: Long, usedBytes: Long): Boolean {
        val thresholdSize = (500 * 1024 * 1024).toLong()
        return totalBytes > thresholdSize
    }

    override fun hashCode(): Int {
        return 38
    }

    override fun equals(other: Any?): Boolean {
        return other is RealmCompactOnLaunchCallback
    }
}