package com.crunchquest.android.presentation.ui.verification

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
        // Set up click listener for the Verify button
        binding.btnVerify.setOnClickListener {
            val code = getCodeFromInput()
            if (code.isNotEmpty()) {
                val userId = intent.getStringExtra("USER_ID") ?: ""
                viewModel.verifyCode(userId, code)
            } else {
                showError("Please enter the verification code.")
            }
        }

        // Set up TextWatchers to automatically move focus to the next EditText
        setupCodeInputFocus()
    }

    private fun setupCodeInputFocus() {
        val editTexts = listOf(
            binding.etCode1, binding.etCode2, binding.etCode3,
            binding.etCode4, binding.etCode5, binding.etCode6
        )

        for (i in editTexts.indices) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && i < editTexts.size - 1) {
                        // Move to the next EditText
                        editTexts[i + 1].requestFocus()
                    } else if (s?.length == 0 && i > 0) {
                        // If the user deletes the character, move back to the previous EditText
                        editTexts[i - 1].requestFocus()
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
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

