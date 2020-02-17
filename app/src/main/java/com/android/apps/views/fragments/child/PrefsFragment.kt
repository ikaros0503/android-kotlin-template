package com.android.apps.views.fragments.child

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.android.apps.BuildConfig
import com.android.apps.R
import com.android.apps.extensions.goStore
import com.android.apps.extensions.openDevPage

class PrefsFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        findPreference<Preference>("rate_us")?.also {
            it.setOnPreferenceClickListener {
                context?.goStore(BuildConfig.APPLICATION_ID)
                true
            }
        }

        findPreference<Preference>("other_apps")?.also {
            it.setOnPreferenceClickListener {
                context?.openDevPage("Kuriboh Studio")
                true
            }
        }

        findPreference<Preference>("version")?.also {
            it.summary = BuildConfig.VERSION_NAME
        }
    }
}