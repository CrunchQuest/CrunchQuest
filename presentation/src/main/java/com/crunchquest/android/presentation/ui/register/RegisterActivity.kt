package com.crunchquest.android.presentation.ui.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.crunchquest.android.presentation.ui.login.LoginActivity
import com.crunchquest.android.presentation.ui.verification.VerificationActivity
import com.crunchquest.shared.R
import com.crunchquest.shared.databinding.ActivityRegisterBinding
import com.crunchquest.android.domain.utility.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewPagerAdapter: RegisterPagerAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        setupListeners()
        observeViewModel()

        binding.buttonNext.isEnabled = false
    }

    private fun setupViewPager() {
        viewPagerAdapter = RegisterPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter

        updateIndicator(0)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.buttonBack.visibility = if (position == 0) View.GONE else View.VISIBLE
                binding.buttonNext.text = if (position == viewPagerAdapter.itemCount - 1) "Register" else "Next"
                updateIndicator(position)
                updateNextButtonState(position)
            }
        })
    }

    private fun updateNextButtonState(position: Int) {
        when (position) {
            0 -> binding.buttonNext.isEnabled = viewModel.isStepOneValid.value == true
            1 -> binding.buttonNext.isEnabled = viewModel.isStepTwoValid.value == true
            2 -> binding.buttonNext.isEnabled = viewModel.isStepThreeValid.value == true
        }
    }

    private fun updateIndicator(position: Int) {
        // Reset all indicators to unfilled
        binding.indicator1.setBackgroundResource(R.drawable.indicator_unfilled)
        binding.indicator2.setBackgroundResource(R.drawable.indicator_unfilled)
        binding.indicator3.setBackgroundResource(R.drawable.indicator_unfilled)

        // Set the current indicator to filled
        when (position) {
            0 -> binding.indicator1.setBackgroundResource(R.drawable.indicator_filled)
            1 -> binding.indicator2.setBackgroundResource(R.drawable.indicator_filled)
            2 -> binding.indicator3.setBackgroundResource(R.drawable.indicator_filled)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupListeners() {
        binding.buttonNext.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem < viewPagerAdapter.itemCount - 1) {
                binding.viewPager.currentItem += 1
            } else {
                if (binding.checkboxTerms.isChecked) {
                    viewModel.registerUser(context = this)
                } else {
                    Toast.makeText(this, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Back Button Click Listener
        binding.buttonBack.setOnClickListener {
            if (binding.viewPager.currentItem > 0) {
                binding.viewPager.currentItem -= 1
            }
        }

        // "Already have an account? Login" Click Listener
        binding.textLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun observeViewModel() {
        viewModel.registrationResult.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(
                        this,
                        "Registration successful! Please check your email to verify your account.",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Navigate to verification or another activity
                    navigateToVerification(userId = result.data)
                }
                is Result.Error -> {
                    Toast.makeText(this, result.exception.message, Toast.LENGTH_SHORT).show()
                }
                Result.Loading -> {
                    // Optionally handle loading state
                }
            }
        }

        // Observe validation state for enabling/disabling the Next button
        viewModel.isStepOneValid.observe(this) { isValid ->
            if (binding.viewPager.currentItem == 0) {
                binding.buttonNext.isEnabled = isValid
            }
        }

        viewModel.isStepTwoValid.observe(this) { isValid ->
            if (binding.viewPager.currentItem == 1) {
                binding.buttonNext.isEnabled = isValid
            }
        }

        viewModel.isStepThreeValid.observe(this) { isValid ->
            if (binding.viewPager.currentItem == 2) {
                binding.buttonNext.isEnabled = isValid
            }
        }
    }


    private fun navigateToVerification(userId: String) {
        val intent = Intent(this, VerificationActivity::class.java)
        intent.putExtra("USER_ID", userId)
        startActivity(intent)
        finish()
    }
}

