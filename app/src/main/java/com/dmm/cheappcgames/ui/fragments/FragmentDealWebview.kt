package com.dmm.cheappcgames.ui.fragments

import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.dmm.cheappcgames.databinding.FragmentDealWebviewBinding
import com.dmm.cheappcgames.utils.Constants.Companion.REDIRECT_URL

class FragmentDealWebview : BaseFragment<FragmentDealWebviewBinding>(
    FragmentDealWebviewBinding::inflate
) {

   private val args: FragmentDealWebviewArgs by navArgs()

    override fun onViewCreated() {
        val id = args.dealId
        id.let {
            val url = "${REDIRECT_URL+it}"
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.apply {
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {
                        binding.progressbar.visibility = View.INVISIBLE
                    }
                }
                loadUrl(url)
            }
        }
    }
}