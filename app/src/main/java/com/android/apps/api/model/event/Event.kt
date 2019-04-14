package com.android.apps.api.model.event

import com.google.gson.annotations.SerializedName

data class Event(
        @SerializedName("_id")
        val id: String,
        val code: String,
        val content: String,
        val image: String,
        val title: String,
        val type: String
)