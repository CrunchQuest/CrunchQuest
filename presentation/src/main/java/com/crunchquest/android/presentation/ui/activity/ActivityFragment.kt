@file:Suppress("DEPRECATION")

package com.crunchquest.android.presentation.ui.activity

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.crunchquest.android.presentation.ui.home.RequestAdapter
import com.crunchquest.shared.R
import com.crunchquest.shared.databinding.FragmentActivityBinding
import com.google.android.material.tabs.TabLayout
import com.crunchquest.android.domain.utility.Result
import com.crunchquest.android.presentation.ui.assistant.AssistAdapter
import com.crunchquest.android.presentation.ui.booking.BookingAdapter
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityFragment : Fragment(R.layout.fragment_activity) {

    private var _binding: FragmentActivityBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ActivityViewModel by viewModels()

    private lateinit var typeAdapter: ActivityTypeAdapter
    private lateinit var requestAdapter: RequestAdapter
    private lateinit var assistAdapter: AssistAdapter
    private lateinit var bookingAdapter: BookingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentActivityBinding.bind(view)

        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        setupToolbar()
        setupTabLayout()
        setupTypeRecyclerView()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.toolbarTitle.text = "Activity"
    }

    private fun setupTabLayout() {
        val tabTitles = listOf("All", "Open", "Pending", "Accepted", "Preparing", "Executing", "Completed", "Cancelled")
        tabTitles.forEach { title ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(title))
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.text?.let { viewModel.setStatusFilter(it.toString()) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupTypeRecyclerView() {
        typeAdapter = ActivityTypeAdapter(ActivityType.values().toList(), viewModel.currentType) { selectedType ->
            viewModel.setTypeFilter(selectedType)
            updateTypeSelection(selectedType)
        }

        binding.typeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = typeAdapter
        }
    }

    private fun updateTypeSelection(selectedType: ActivityType) {
        typeAdapter = ActivityTypeAdapter(ActivityType.values().toList(), selectedType) { newType ->
            viewModel.setTypeFilter(newType)
            updateTypeSelection(newType)
        }
        binding.typeRecyclerView.adapter = typeAdapter
    }

    private fun setupRecyclerView() {
        requestAdapter = RequestAdapter()
        assistAdapter = AssistAdapter()
        bookingAdapter = BookingAdapter()

        binding.recyclerView.apply {
            adapter = requestAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeViewModel() {
        viewModel.filteredActivities.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoading()
                is Result.Success -> showData(result.data)
                is Result.Error -> showError(result.exception)
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.emptyStateTextView.visibility = View.GONE
    }

    private fun showData(data: List<ActivityItem>) {
        binding.progressBar.visibility = View.GONE
        if (data.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
            binding.emptyStateTextView.visibility = View.VISIBLE
            binding.emptyStateTextView.text = when (viewModel.currentType) {
                ActivityType.REQUEST -> "No requests available."
                ActivityType.ASSIST -> "No assists available."
                ActivityType.BOOKING -> "No bookings available."
            }
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.emptyStateTextView.visibility = View.GONE
            when (viewModel.currentType) {
                ActivityType.REQUEST -> {
                    requestAdapter.submitList(data.filterIsInstance<ActivityItem.RequestItem>().map { it.request })
                    binding.recyclerView.adapter = requestAdapter
                }
                ActivityType.ASSIST -> {
                    assistAdapter.submitList(data.filterIsInstance<ActivityItem.AssistItem>().map { it.assistant })
                    binding.recyclerView.adapter = assistAdapter
                }
                ActivityType.BOOKING -> {
                    bookingAdapter.submitList(data.filterIsInstance<ActivityItem.BookingItem>().map { it.booking })
                    binding.recyclerView.adapter = bookingAdapter
                }
            }
        }
    }

    private fun showError(exception: Throwable) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.emptyStateTextView.visibility = View.VISIBLE
        binding.emptyStateTextView.text = "Error: ${exception.message}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
