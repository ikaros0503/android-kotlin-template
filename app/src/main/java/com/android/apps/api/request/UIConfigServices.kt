package com.android.apps.api.request

import com.android.apps.api.model.uiconfig.UIConfig
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface UIConfigServices {

    @GET("/uiconfig/free.app.music.player.songs.json")
    fun fetchConfig(): Flowable<UIConfig>

    companion object {
        private var instance: UIConfigServices? = null

        val default: UIConfigServices = instance ?: synchronized(UIConfigServices::class.java) {
            instance ?: Retrofit.Builder()
                    .baseUrl("https://s3-ap-southeast-1.amazonaws.com/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(UIConfigServices::class.java).also {
                        instance = it
                    }
        }
    }
}