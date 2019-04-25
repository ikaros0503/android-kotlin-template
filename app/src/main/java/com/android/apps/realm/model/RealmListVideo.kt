package com.android.apps.realm.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import java.util.*

open class RealmListVideo : RealmObject() {
    @Index
    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
    var items: RealmList<RealmVideo> = RealmList()
}