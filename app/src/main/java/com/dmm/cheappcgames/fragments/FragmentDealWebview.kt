package com.dmm.cheappcgames.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.databinding.FragmentDealWebviewBinding
import com.dmm.cheappcgames.utils.Constants.Companion.REDIRECT_URL

class FragmentDealWebview : Fragment(R.layout.fragment_deal_webview) {

    private lateinit var _binding: FragmentDealWebviewBinding
    private val binding get() = _binding!!

    private val args: FragmentDealWebviewArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDealWebviewBinding.bind(view)
        val id = args.dealId
        id.let {
            val url = "${REDIRECT_URL+it}"
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.apply {
                webViewClient = WebViewClient()
                loadUrl(url)
            }
        }

    }
}