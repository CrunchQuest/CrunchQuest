package com.crunchquest.android.presentation.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.crunchquest.shared.R
import com.crunchquest.shared.databinding.FragmentRegisterStepOneBinding

class RegisterStepOneFragment : Fragment() {

    private val viewModel: RegisterViewModel by activityViewModels()
    private var _binding: FragmentRegisterStepOneBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterStepOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            // Observe ViewModel LiveData and update UI
            viewModel.firstName.observe(viewLifecycleOwner) { firstName ->
                if (editTextFirstName.text.toString() != firstName) {
                    editTextFirstName.setText(firstName)
                }
            }

            viewModel.lastName.observe(viewLifecycleOwner) { lastName ->
                if (editTextLastName.text.toString() != lastName) {
                    editTextLastName.setText(lastName)
                }
            }

            viewModel.email.observe(viewLifecycleOwner) { email ->
                if (editTextEmail.text.toString() != email) {
                    editTextEmail.setText(email)
                }
            }

            // Update ViewModel when text changes
            editTextFirstName.addTextChangedListener { text ->
                viewModel.firstName.value = text.toString()
                viewModel.validateStepOne()
            }

            editTextLastName.addTextChangedListener { text ->
                viewModel.lastName.value = text.toString()
                viewModel.validateStepOne()
            }

            editTextEmail.addTextChangedListener { text ->
                viewModel.email.value = text.toString()
                viewModel.validateStepOne()
            }
        }

        viewModel.validateStepOne()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
