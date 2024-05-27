package com.example.crunchquest.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.crunchquest.R
import com.example.crunchquest.ui.buyer.BuyerActivity

class PaymentActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbarPayment)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Payment"

        // Set up WebView
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
        super.onBackPressed()
        navigateToHomeFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle the back button click
                navigateToHomeFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToHomeFragment() {
        // Replace the current fragment with HomeFragment
        // Create an Intent to navigate back to BuyerActivity
        val intent = Intent(this, BuyerActivity::class.java)
        startActivity(intent)
        finish()
    }
}