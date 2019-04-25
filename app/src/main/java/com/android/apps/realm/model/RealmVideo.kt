package com.android.apps.realm.model

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

open class RealmVideo : RealmObject() {

    @Index
    @PrimaryKey
    var id: String = ""

    var title: String = ""
    var url: String = ""
    var localURL: String = ""
    var type: Int = Type.FACEBOOK.ordinal

    enum class Type {
        FACEBOOK
    }
}