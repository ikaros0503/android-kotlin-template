package com.android.apps.api.request

import com.android.apps.api.model.event.Event
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @GET("/events")
    fun listEvents(@Query("conditions[type]") type: String = "recent_playing",
                   @Query("options[limit]") limit: Int = 20
    ): Flowable<List<Event>>

    @GET("/events/{id}")
    fun getEvent(@Path("id") eventId: String): Flowable<Event>

    companion object {
        @Volatile private var instance: ApiServices? = null

        val default: ApiServices = instance ?: synchronized(ApiServices::class.java) {
            instance ?: Retrofit.Builder()
                    .baseUrl("http://apiv1.glustack.com/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ApiServices::class.java).also {
                        instance = it
                    }

        }
    }
}