package com.android.apps.content

import java.io.File

data class FBVideoContent(
        val id: String,
        val url: String
) {
    fun getTitleFromURL(): String {
        return File(url).let {
            if (!it.exists()) url
            else {
                it.name
            }
        }
    }
}