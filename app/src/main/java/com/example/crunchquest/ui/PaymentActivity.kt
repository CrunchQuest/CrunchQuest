package com.example.crunchquest.ui

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.crunchquest.R

class PaymentActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        webView = findViewById(R.id.wvPayment)

        val paymentUrl = intent.getStringExtra("checkoutUrl")
        paymentUrl?.let {
            Log.d("PaymentActivity", "Payment URL received: $it")
            webView.webViewClient = WebViewClient()
            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true
            webView.loadUrl(it)
            Log.d("PaymentActivity", "Loading URL in WebView: $it")
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
            finish()
        }
    }
}