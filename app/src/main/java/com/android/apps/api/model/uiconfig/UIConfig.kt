package com.android.apps.api.model.uiconfig

import com.google.gson.annotations.SerializedName

data class UIConfig(
    val isEnableCartoonTab: Boolean,
    val enableRatingFunction: Boolean,
    @SerializedName("disable_ad")
    val disabledAds: List<String>,
    @SerializedName("is_testing")
    val isTesting: Int
)