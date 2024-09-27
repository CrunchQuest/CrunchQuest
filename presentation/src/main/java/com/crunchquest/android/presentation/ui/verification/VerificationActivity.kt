package com.crunchquest.android.presentation.ui.verification

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.crunchquest.android.presentation.ui.MainActivity
import com.crunchquest.shared.databinding.ActivityVerificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerificationBinding
    private val viewModel: VerificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.verificationState.observe(this) { state ->
            when (state) {
                is VerificationState.Loading -> showLoading()
                is VerificationState.Success -> navigateToHome()
                is VerificationState.Error -> showError(state.message)
            }
        }
    }

    private fun setupListeners() {
        binding.btnVerify.setOnClickListener {
            val code = getCodeFromInput()
            if (code.isNotEmpty()) {
                val userId = intent.getStringExtra("USER_ID") ?: ""
                viewModel.verifyCode(userId, code)
            } else {
                showError("Please enter the verification code.")
            }
        }
    }

    private fun getCodeFromInput(): String {
        return binding.etCode1.text.toString() +
                binding.etCode2.text.toString() +
                binding.etCode3.text.toString() +
                binding.etCode4.text.toString() +
                binding.etCode5.text.toString() +
                binding.etCode6.text.toString()
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnVerify.isEnabled = false
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.btnVerify.isEnabled = true
    }

    private fun showError(message: String) {
        hideLoading()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToHome() {
        hideLoading()
        Toast.makeText(this, "Verification Successful", Toast.LENGTH_SHORT).show()
        // Navigate to Home Activity or next screen
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

