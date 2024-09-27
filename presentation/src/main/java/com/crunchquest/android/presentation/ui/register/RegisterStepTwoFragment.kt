package com.crunchquest.android.presentation.ui.register

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.crunchquest.android.presentation.utility.PasswordValidation
import com.crunchquest.shared.R
import com.crunchquest.shared.databinding.FragmentRegisterStepTwoBinding

// RegisterStepTwoFragment.kt
class RegisterStepTwoFragment : Fragment() {

    private val viewModel: RegisterViewModel by activityViewModels()
    private var _binding: FragmentRegisterStepTwoBinding? = null
    private val binding get() = _binding!!

    private val passwordValidation = PasswordValidation()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterStepTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n") // Suppress warning for setting text programmatically
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            // Observe ViewModel LiveData and update UI
            viewModel.password.observe(viewLifecycleOwner) { password ->
                if (editTextPassword.text.toString() != password) {
                    editTextPassword.setText(password)
                }
                validatePassword(password)
            }

            viewModel.confirmPassword.observe(viewLifecycleOwner) { confirmPassword ->
                if (editTextConfirmPassword.text.toString() != confirmPassword) {
                    editTextConfirmPassword.setText(confirmPassword)
                }
            }

            // Update ViewModel when text changes
            editTextPassword.addTextChangedListener { text ->
                val password = text.toString()
                viewModel.password.value = password
                validatePassword(password)
                viewModel.validateStepTwo()
            }

            editTextConfirmPassword.addTextChangedListener { text ->
                viewModel.confirmPassword.value = text.toString()
                viewModel.validateStepTwo()
            }
        }

        viewModel.validateStepTwo()
    }

    private fun validatePassword(password: String) {
        val validations = passwordValidation.validate(password)

        binding.apply {
            textValidationUppercase.setTextColor(getValidationColor(validations.hasUppercase))
            textValidationLowercase.setTextColor(getValidationColor(validations.hasLowercase))
            textValidationNumber.setTextColor(getValidationColor(validations.hasNumber))
            textValidationSpecialChar.setTextColor(getValidationColor(validations.hasSpecialChar))
            textValidationLength.setTextColor(getValidationColor(validations.isLengthValid))
        }
    }

    private fun getValidationColor(isValid: Boolean): Int {
        return if (isValid) {
            ContextCompat.getColor(requireContext(), R.color.cool_green)
        } else {
            ContextCompat.getColor(requireContext(), R.color.cool_red)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

