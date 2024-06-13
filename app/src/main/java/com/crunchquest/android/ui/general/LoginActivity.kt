package com.crunchquest.android.ui.general

import android.content.Intent
import android.os.Bundle
<<<<<<< HEAD:app/src/main/java/com/crunchquest/android/ui/general/LoginActivity.kt
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
=======
>>>>>>> 4c87e12 ([Auth] Migrate SignUp and Login To MVVM):androidApp/src/main/java/com/crunchquest/android/ui/general/LoginActivity.kt
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.crunchquest.android.databinding.ActivityLoginBinding
import com.crunchquest.android.repository.UserRepository
import com.crunchquest.android.ui.buyer.BuyerActivity
import com.crunchquest.android.ui.dialogs.ResetPassword
<<<<<<< HEAD:app/src/main/java/com/crunchquest/android/ui/general/LoginActivity.kt
import com.google.android.material.textfield.TextInputLayout
=======
import com.crunchquest.android.utility.buttonAlphaEnabledListener
import com.crunchquest.android.viewmodel.LoginViewModel
import com.crunchquest.android.viewmodel.ViewModelFactory
>>>>>>> 4c87e12 ([Auth] Migrate SignUp and Login To MVVM):androidApp/src/main/java/com/crunchquest/android/ui/general/LoginActivity.kt
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

<<<<<<< HEAD:app/src/main/java/com/crunchquest/android/ui/general/LoginActivity.kt
        auth = FirebaseAuth.getInstance()
        //Map the views from the layout file
        tvLoginHeading = findViewById<TextView>(R.id.tvLoginHeading)
        tvLoginSubHeading = findViewById<TextView>(R.id.tvLoginSubHeading)
        logInBtn = findViewById<Button>(R.id.btnLogin)
        val signUpTextView = findViewById<TextView>(R.id.tvSignup)
        val forgotPass = findViewById<TextView>(R.id.tvForgotPassword)
        val emailTextInputLayout = findViewById<TextInputLayout>(R.id.etEmailContainer)
        val passwordTextInputLayout = findViewById<TextInputLayout>(R.id.etPasswordContainer)

        emailEditText = emailTextInputLayout.editText!!
        passwordEditText = passwordTextInputLayout.editText!!

        //Log in button
        logInBtn.setOnClickListener {
            doLogin()
        }
        //Sign up text view that is clickable
        signUpTextView.setOnClickListener {
            numberOrEmail()
        }
        forgotPass.setOnClickListener {
            val dialog = ResetPassword(this)
            dialog.startLoadingAnimation()
        }

        checkIfAnAccountIsLoggedIn()
        buttonAlphaEnabledListener()

=======
        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val userRepository = UserRepository(auth, database)

        val viewModelFactory = ViewModelFactory(userRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
>>>>>>> 4c87e12 ([Auth] Migrate SignUp and Login To MVVM):androidApp/src/main/java/com/crunchquest/android/ui/general/LoginActivity.kt

        setupViews()
        observeLoginResult()
    }

    private fun setupViews() {
        binding.btnLogin.setOnClickListener { doLogin() }
        binding.tvSignup.setOnClickListener { navigateToSignUp() }
        binding.tvForgotPassword.setOnClickListener { showResetPasswordDialog() }

        buttonAlphaEnabledListener(binding.etEmailContainer.editText!!, binding.etPasswordContainer.editText!!, binding.btnLogin)
    }

    private fun observeLoginResult() {
        viewModel.loginResult.observe(this, Observer { success ->
            if (success) {
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    if (currentUser.isEmailVerified) {
                        checkUserPreferences(currentUser.uid)
                    } else {
                        showErrorToast("Please verify your email address.")
                        FirebaseAuth.getInstance().signOut()
                    }
                } else {
                    showErrorToast("Incorrect email or password.")
                }
            } else {
                showErrorToast("Login failed. Please check your credentials.")
            }
        })
    }

    private fun doLogin() {
        val email = binding.etEmailContainer.editText?.text.toString()
        val password = binding.etPasswordContainer.editText?.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            showErrorToast("Please enter email and password")
            return
        }

        viewModel.loginUser(email, password)
    }

    private fun navigateToSignUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun showResetPasswordDialog() {
        val dialog = ResetPassword(this)
        dialog.startLoadingAnimation()
    }

    private fun checkUserPreferences(uid: String) {
        viewModel.checkUserPreferences(uid).observe(this, Observer { preferencesExist ->
            val intent = if (preferencesExist) {
                Intent(this@LoginActivity, BuyerActivity::class.java)
            } else {
                Intent(this@LoginActivity, PersonalizationActivity::class.java)
            }
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        })
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // No need to unbind here
    }
}
