package com.android.apps.views.fragments.main

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.android.apps.BuildConfig
import com.android.apps.R
import com.android.apps.extensions.goStore
import com.android.apps.extensions.openDevPage
import com.android.apps.views.fragments.BaseFragment

class SettingFragment: BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_settings
}