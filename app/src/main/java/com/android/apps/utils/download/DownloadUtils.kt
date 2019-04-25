package com.android.apps.utils.download

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.android.apps.content.FBVideoContent
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class DownloadUtils(private val context: Context) {

    private val downloadService = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    fun download(url: String) {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setTitle("FBDownloader")
        val currentDate = SimpleDateFormat("DD_mm_yyyy_HH_mm", Locale.getDefault()).format(Date())
        val fileName = "FBVideo-$currentDate.mp4"
        request.setDescription(fileName)
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
        downloadService.enqueue(request)
    }

    fun getDownloadFolderPath(): String {
        return context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.absolutePath
    }

    fun fetchFileFromDownloadFolder(): Observable<List<FBVideoContent>> {
        return Observable.create<List<FBVideoContent>> { emitter ->
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.let { folder ->
                val result = mutableListOf<FBVideoContent>()
                for (file in folder.listFiles()) {
                    result.add(FBVideoContent("", file.absolutePath))
                }
                emitter.onNext(result)
            }
            emitter.onComplete()
        }.subscribeOn(Schedulers.io())
    }

    companion object {


        @SuppressLint("StaticFieldLeak")
        private var instance: DownloadUtils? = null

        fun get(context: Context): DownloadUtils = instance
            ?: synchronized(DownloadUtils::class.java) {
                instance ?: DownloadUtils(context).also {
                    instance = it
                }
            }
    }
}