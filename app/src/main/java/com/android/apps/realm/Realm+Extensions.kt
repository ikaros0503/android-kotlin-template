package com.android.apps.realm

import android.content.Context
import com.android.apps.BuildConfig
import com.android.apps.realm.compact.RealmCompactOnLaunchCallback
import com.android.apps.realm.migration.Migration
import com.android.apps.realm.model.RealmListVideo
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.*

object RealmUtils {

    private val schemaVersion = 0L

    fun initialize(context: Context) {
        Realm.init(context)
        val configuration = RealmConfiguration.Builder()
                .schemaVersion(schemaVersion)
                .compactOnLaunch(RealmCompactOnLaunchCallback())
                .let {
                    if (BuildConfig.DEBUG) {
                        it.migration(Migration())
                    } else {
                        it.deleteRealmIfMigrationNeeded()
                    }
                }
                .build()
        Realm.setDefaultConfiguration(configuration)
        postInitialize()
    }

    private fun postInitialize() {
        Realm.getDefaultInstance().use { realm ->
            // Create download list of not
            realm.beginTransaction()
            realm.where(RealmListVideo::class.java).findFirst() ?: kotlin.run {
                realm.createObject(RealmListVideo::class.java, UUID.randomUUID().toString())
            }
            realm.commitTransaction()
        }
    }
}