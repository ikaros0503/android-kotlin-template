package com.android.apps.views.fragments.main

import android.os.Build
import android.util.Log
import android.webkit.*
import com.android.apps.R
import com.android.apps.repository.uiconfig.UIConfigRepository
import com.android.apps.views.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_web.*

class WebFragment : BaseFragment() {

    private val configRepository by lazy {
        UIConfigRepository.instance
    }

    override fun getLayoutId(): Int = R.layout.fragment_web

    override fun initialViewComponent() {
        with(webview_fragment) {
            val webSettings = settings

            webSettings.allowFileAccessFromFileURLs = true
            webSettings.allowUniversalAccessFromFileURLs = true
            webSettings.domStorageEnabled = true
            webSettings.javaScriptCanOpenWindowsAutomatically = true
            webSettings.javaScriptEnabled = true


            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                    Log.w("WebView", consoleMessage?.message())
                    return super.onConsoleMessage(consoleMessage)
                }
            }
            webViewClient = object : WebViewClient() {
                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    Log.w("WebView", "Error:$error")
                }

                override fun onLoadResource(view: WebView?, url: String?) {
                    super.onLoadResource(view, url)
                    executeJSScript(configRepository.config.webview.script)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Log.w("WebView", "LoadedFinished")
                    executeJSScript(configRepository.config.webview.script)
                }
            }

            configRepository.config.webview.url.takeIf { it.isNotEmpty() }?.also {
                loadUrl(it)
            }
        }
    }

    private fun executeJSScript(script: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webview_fragment?.evaluateJavascript(script, null)
        } else {
            webview_fragment?.loadUrl("javascript:$script")
        }
    }

    fun canGoBack() = webview_fragment.canGoBack()

    override fun onBackPressed() {
        webview_fragment?.goBack()
    }
}