package com.android.apps.components

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.util.Log
import android.webkit.*
import com.android.apps.content.FBVideoContent
import com.android.apps.extensions.OnFBVideoProcessed
import com.android.apps.extensions.OnWebviewURLChangeListener

open class FBDownloadWebView : WebView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleInt: Int) : super(context, attrs, defStyleInt)

    private var onFBVideoProcessed: OnFBVideoProcessed? = null
    private var onWebViewURLChangeListener: OnWebviewURLChangeListener? = null

    init {
        initializeWebView()
    }

    private fun initializeWebView() {
        settings.apply {
            databaseEnabled = true
            javaScriptEnabled = true
            allowFileAccessFromFileURLs = true
            allowUniversalAccessFromFileURLs = true
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            userAgentString = "Mozilla/5.0 (Linux; Android 8.0.0; TA-1053 Build/OPR1.170623.026) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3368.0 Mobile Safari/537.36"
        }
        addJavascriptInterface(this, JS_INTERFACE_NAME)
        webViewClient = object : WebViewClient() {
            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
                loadUrl("javascript:(function() { "
                        + "var el = document.querySelectorAll('div[data-sigil]');"
                        + "for(var i=0;i<el.length; i++)"
                        + "{"
                        + "var sigil = el[i].dataset.sigil;"
                        + "if(sigil.indexOf('inlineVideo') > -1){"
                        + "var jsonData = JSON.parse(el[i].dataset.store);"
                        + "var src = jsonData.src; var videoId = jsonData.videoId;"
                        + "var videoId = jsonData.videoID;"
                        + "el[i].setAttribute('onClick', '$JS_INTERFACE_NAME.processVideo(\"'+src+'\", \"'+videoId+ '\");');"
                        + "}" + "}" + "})()")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                loadUrl("javascript:(window.onload = function() { "
                        + "console.log('onloaded');"
                        + "var el = document.querySelectorAll('div[data-sigil]');"
                        + "for(var i=0;i<el.length; i++)"
                        + "{"
                        + "var sigil = el[i].dataset.sigil;"
                        + "if(sigil.indexOf('inlineVideo') > -1){"
                        + "var jsonData = JSON.parse(el[i].dataset.store);"
                        + "var src = jsonData.src; var videoId = jsonData.videoID;"
                        + "var videoId = jsonData.videoId;"
                        + "el[i].setAttribute('onClick', '$JS_INTERFACE_NAME.processVideo(\"'+src+'\", \"'+videoId+ '\");');"
                        + "}" + "}" + "})()")
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest?): Boolean {
                onWebViewURLChangeListener?.invoke(view.url)
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                onWebViewURLChangeListener?.invoke(url!!)
            }
        }

        webChromeClient = object : WebChromeClient() {

        }

        loadUrl("https://m.facebook.com")
    }

    fun registerOnVideoProcessed(action: OnFBVideoProcessed) {
        onFBVideoProcessed = action
    }

    fun registerOnURLChange(action: OnWebviewURLChangeListener) {
        onWebViewURLChangeListener = action
    }


    @JavascriptInterface
    fun processVideo(url: String, videoId: String) {
        post { onFBVideoProcessed?.invoke(FBVideoContent(videoId, url)) }
    }

    companion object {
        private const val JS_INTERFACE_NAME = "FBDownloader"
    }
}