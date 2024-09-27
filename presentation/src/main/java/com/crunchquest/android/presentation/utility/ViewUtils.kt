package com.crunchquest.android.presentation.utility

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText

fun buttonAlphaEnabledListener(emailEditText: EditText, passwordEditText: EditText, logInBtn: Button) {
    fun updateButtonState() {
        if (emailEditText.text.toString().isEmpty() || passwordEditText.text.toString().isEmpty()) {
            logInBtn.alpha = 0.4f
            logInBtn.isEnabled = false
        } else {
            logInBtn.alpha = 1f
            logInBtn.isEnabled = true
        }
    }

    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            updateButtonState()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    emailEditText.addTextChangedListener(textWatcher)
    passwordEditText.addTextChangedListener(textWatcher)

    // Initial call to set the button state
    updateButtonState()
}
