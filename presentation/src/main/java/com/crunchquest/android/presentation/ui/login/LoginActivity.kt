package com.crunchquest.android.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.crunchquest.android.domain.entities.User
import com.crunchquest.android.presentation.ui.MainActivity
import com.crunchquest.android.presentation.ui.adapter.BannerAdapter
import com.crunchquest.android.presentation.ui.register.RegisterActivity
import com.crunchquest.android.presentation.utility.UserSessionManager
import com.crunchquest.shared.R
import com.crunchquest.shared.databinding.ActivityLoginBinding
import com.crunchquest.shared.databinding.FragmentForgotPassBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var binding: ActivityLoginBinding
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var indicatorViews: List<View>

    private lateinit var userSessionManager: UserSessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UserSessionManager.initialize(applicationContext)

        // Set up custom indicator views
        indicatorViews = listOf(
            binding.indicator1,
            binding.indicator2,
            binding.indicator3,
            binding.indicator4
        )

        // Set up ViewPager2 (Slider)
        setupBannerSlider()

        // Handle login button click
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty()) {
                binding.etEmailContainer.error = "Email is required"
            } else if (password.isEmpty()) {
                binding.etPasswordContainer.error = "Password is required"
            } else {
                // Clear previous errors
                binding.etEmailContainer.error = null
                binding.etPasswordContainer.error = null

                // Trigger login operation
                loginViewModel.login(email, password)
            }
        }

        // Observe the login state and update UI accordingly
        loginViewModel.loginState.observe(this) { state ->
            when (state) {
                is LoginState.Loading -> showLoading()
                is LoginState.Success -> {
                    hideLoading()
                    handleLoginSuccess(state.user)
                }
                is LoginState.Error -> {
                    hideLoading()
                    showError(state.message)
                }
            }
        }

        // Handle forgot password click
        binding.tvForgotPassword.setOnClickListener {
            // Navigate to Forgot Password screen
            startActivity(Intent(this, FragmentForgotPassBinding::class.java))
        }

        // Handle sign-up click
        binding.tvSignup.setOnClickListener {
            // Navigate to Sign-Up screen
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.btnLogin.isEnabled = true // Re-enable login button after loading
    }

    private fun setupBannerSlider() {
        bannerAdapter = BannerAdapter(getBannerImages())
        binding.vpBanner.adapter = bannerAdapter

        // Set the first indicator as filled
        setIndicator(0)

        // Update the custom indicators when the page is changed
        binding.vpBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setIndicator(position)
            }
        })

        // Optional: Auto-slide functionality
        val autoSlider = Runnable {
            val nextItem = (binding.vpBanner.currentItem + 1) % bannerAdapter.itemCount
            binding.vpBanner.currentItem = nextItem
        }
        val handler = Handler(Looper.getMainLooper())
        binding.vpBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                handler.removeCallbacks(autoSlider)
                handler.postDelayed(autoSlider, 3000) // Auto-slide after 3 seconds
            }
        })
    }

    private fun setIndicator(position: Int) {
        for (i in indicatorViews.indices) {
            // Change color based on whether the indicator is active (filled) or inactive (unfilled)
            val color = if (i == position) R.color.orange else R.color.washed_orange
            indicatorViews[i].setBackgroundColor(ContextCompat.getColor(this, color))
        }
    }

    private fun getBannerImages(): List<Int> {
        // Replace with actual banner image resources
        return listOf(R.drawable.banner_1, R.drawable.banner_2, R.drawable.food11, R.drawable.food1)
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnLogin.isEnabled = false // Disable login button while loading
    }

    private fun handleLoginSuccess(user: User?) {
        if (user != null) {
            UserSessionManager.saveOrUpdateUser(user)  // Ensure user session is updated
            Log.d("LoginActivity", "User logged in: $user")
            navigateToHome()
        } else {
            Log.e("LoginActivity", "Login failed, user is null")
            showError("Login failed. User data is null.")
        }
    }

    private fun navigateToHome() {
        binding.progressBar.visibility = View.GONE
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun showError(message: String?) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(this, message ?: "Login failed", Toast.LENGTH_SHORT).show()
    }
}

