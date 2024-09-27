package com.crunchquest.android.presentation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.crunchquest.shared.R
import com.crunchquest.android.domain.entities.User
import com.crunchquest.shared.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import android.content.Intent as Intent1


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by viewModels()

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the profile state
        profileViewModel.profileState.observe(viewLifecycleOwner, { state ->
            when (state) {
                is ProfileState.Loading -> showLoading()
                is ProfileState.Success -> showUserProfile(state.user)
                is ProfileState.Error -> showError(state.message)
            }
        })

        // Fetch user profile
        profileViewModel.fetchUserProfile(userId = "12345") // Use the actual user ID here
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showUserProfile(user: User?) {
        binding.progressBar.visibility = View.GONE
        user?.let {
            binding.tvUsername.text = it.firstName + " " + it.lastName
            // Set other profile details as needed
        }
    }

    private fun showError(message: String?) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(requireContext(), message ?: "Unknown error", Toast.LENGTH_SHORT).show()
    }
}
