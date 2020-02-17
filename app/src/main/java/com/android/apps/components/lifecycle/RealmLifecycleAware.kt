package com.android.apps.components.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
//import io.realm.Realm

//class RealmLifecycleAware(instance: Realm) : LifecycleAware<Realm>(instance) {
//    override fun close() {
//        instance.close()
//    }
//}
//
//fun LifecycleOwner.ownRealm(realm: Realm) {
//    if (this.lifecycle.currentState == Lifecycle.State.DESTROYED) {
//        realm.close()
//        return
//    }
//    this.lifecycle.addObserver(RealmLifecycleAware(realm))
//}
//
//fun Realm.attachToLifeCycle(lifecycleOwner: LifecycleOwner) {
//    lifecycleOwner.ownRealm(this)
//}