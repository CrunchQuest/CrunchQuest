package com.crunchquest.android.utility

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText

fun buttonAlphaEnabledListener(emailEditText: EditText, passwordEditText: EditText, logInBtn: Button) {
    if (emailEditText.text.toString().isEmpty() || passwordEditText.text.toString().isEmpty()) {
        logInBtn.alpha = 0.4f
        logInBtn.isEnabled = false
    } else {
        logInBtn.alpha = 1f
        logInBtn.isEnabled = true
    }
    emailEditText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            buttonAlphaEnabledListener(emailEditText, passwordEditText, logInBtn)
        }

        override fun afterTextChanged(s: Editable?) {
        }
    })
    passwordEditText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            buttonAlphaEnabledListener(emailEditText, passwordEditText, logInBtn)
        }

        override fun afterTextChanged(s: Editable?) {
        }
    })
}