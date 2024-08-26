package com.machine.machine.ui.user.screen.setting

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.R
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.constant.BaseConstants
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.FragmentUWebviewBinding


/**
 * Created by Amit Rawat on 4/1/2022.
 */
class WebViewFragment : BaseFragramentLoaderVM<FragmentUWebviewBinding>() {


    private var webURL: String = BaseConstants.EMPTY
    private var urlId: Int = 0
    private var title: String = BaseConstants.EMPTY
    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUWebviewBinding.inflate(inflater, container, false)

    override fun appHeader() {
        urlId = requireArguments().getInt(IDConst.INTENT_ID)
        setUrl()
        EventBus.actionBarTitle(title)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun setup() {
        binding.webview.settings.javaScriptEnabled = true

        if (!isOnline()) {
            noInternet()
            return
        } else {
            loadWebView()
        }
    }

    private fun setUrl() {
        when (urlId) {
            0 -> {
                title = getString(R.string.about_us)
                webURL = BaseConstants.ABOUT_US_URL
            }
            1 -> {
                title = getString(R.string.faq)
                webURL = BaseConstants.FAQ
            }
            2 -> {
                title = getString(R.string.privacy_policy)
                webURL = BaseConstants.PRIVACY_POLICY_URL
            }
            else -> {
                title = getString(R.string.about_us)
                webURL = BaseConstants.ABOUT_US_URL
            }

        }
    }

    override fun onRetrybtn() {
        loadWebView()
    }

    override fun onDestroyView() {

        super.onDestroyView()
    }

    private fun loadWebView() {
        binding.webview.loadUrl(webURL)
        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                showContent()
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                showError(IDConst.SOMETHING_WENT_WRONG)
                val errorMessage = "Got Error! $error"

                super.onReceivedError(view, request, error)
            }
        }
    }


    private fun isOnline(): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }


    override fun viewModelObj() {
    }

    override fun viewModelObserver() {
    }

    override fun viewClick() {
    }


}



