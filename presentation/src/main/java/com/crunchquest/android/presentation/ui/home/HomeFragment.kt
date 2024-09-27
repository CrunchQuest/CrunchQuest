package com.crunchquest.android.presentation.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.crunchquest.android.domain.utility.Result
import com.crunchquest.android.presentation.ui.adapter.BannerAdapter
import com.crunchquest.android.presentation.ui.request.CreateRequestActivity
import com.crunchquest.shared.R
import com.crunchquest.shared.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var requestAdapter: RequestAdapter
    private lateinit var providerAdapter: ProviderAdapter
    private lateinit var recentActivityAdapter: RecentActivityAdapter

    private val handler = Handler(Looper.getMainLooper())
    private var autoSlider: Runnable? = null

    // View binding property
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view) // Initialize view binding

        setupBannerViewPager()
        setupAdapters()
        setupRecyclerViews()
        observeViewModel()

        binding.fabCreateRequest.setOnClickListener {
            navigateToCreateRequestActivity()
        }

        setupSwipeRefreshLayouts()
    }

    private fun navigateToCreateRequestActivity() {
        val intent = Intent(requireContext(), CreateRequestActivity::class.java)
        startActivity(intent)
    }

    private fun setupBannerViewPager() {
        // Initialize the BannerAdapter with your banner images
        val bannerImages = listOf(R.drawable.food8, R.drawable.food10, R.drawable.food2)
        val bannerAdapter = BannerAdapter(bannerImages)

        // Set the adapter to the ViewPager2
        binding.bannerViewPager.adapter = bannerAdapter

        // Set up auto-slide functionality
        setupAutoSlider(bannerAdapter.itemCount)
    }

    private fun setupAutoSlider(itemCount: Int) {
        autoSlider?.let { handler.removeCallbacks(it) }

        autoSlider = object : Runnable {
            override fun run() {
                _binding?.let {
                    val nextItem = (it.bannerViewPager.currentItem + 1) % itemCount
                    it.bannerViewPager.currentItem = nextItem
                    handler.postDelayed(this, 3000) // Auto-slide every 3 seconds
                }
            }
        }

        handler.postDelayed(autoSlider!!, 3000)

        // Registering callback to reset the timer when the user interacts with the ViewPager2
        binding.bannerViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                autoSlider?.let { handler.removeCallbacks(it) }
                handler.postDelayed(autoSlider!!, 3000)
            }
        })
    }

    private fun setupAdapters() {
        requestAdapter = RequestAdapter()
        providerAdapter = ProviderAdapter()
        recentActivityAdapter = RecentActivityAdapter()
    }

    private fun setupRecyclerViews() {
        binding.recyclerViewFeaturedRequests.adapter = requestAdapter
        binding.recyclerViewServiceProviders.adapter = providerAdapter
        binding.recyclerViewRecentActivity.adapter = recentActivityAdapter
    }

    private fun setupSwipeRefreshLayouts() {
        binding.swipeRefreshLayoutFeaturedRequests.setOnRefreshListener {
            viewModel.loadFeaturedRequests()
            binding.swipeRefreshLayoutFeaturedRequests.isRefreshing = false
        }

        binding.swipeRefreshLayoutServiceHeroes.setOnRefreshListener {
            viewModel.loadServiceHeroes()
            binding.swipeRefreshLayoutServiceHeroes.isRefreshing = false
        }
    }

    private fun observeViewModel() {
        // Observe featured requests
        viewModel.featuredRequests.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.featuredRequestsProgressBar.visibility = View.VISIBLE
                    binding.recyclerViewFeaturedRequests.visibility = View.GONE
                    binding.tvEmptyFeaturedRequests.visibility = View.GONE
                }
                is Result.Success -> {
                    binding.featuredRequestsProgressBar.visibility = View.GONE
                    binding.recyclerViewFeaturedRequests.visibility = View.VISIBLE
                    requestAdapter.submitList(result.data)
                    toggleEmptyState(binding.recyclerViewFeaturedRequests, binding.tvEmptyFeaturedRequests, result.data)
                }
                is Result.Error -> {
                    binding.featuredRequestsProgressBar.visibility = View.GONE
                    binding.recyclerViewFeaturedRequests.visibility = View.GONE
                    binding.tvEmptyFeaturedRequests.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Failed to load featured requests: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                }

            }
        }

        // Observe service heroes (providers)
        viewModel.serviceHeroes.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.serviceHeroesProgressBar.visibility = View.VISIBLE
                    binding.recyclerViewServiceProviders.visibility = View.GONE
                    binding.tvEmptyServiceHeroes.visibility = View.GONE
                }
                is Result.Success -> {
                    binding.serviceHeroesProgressBar.visibility = View.GONE
                    binding.recyclerViewServiceProviders.visibility = View.VISIBLE
                    providerAdapter.submitList(result.data)
                    toggleEmptyState(binding.recyclerViewServiceProviders, binding.tvEmptyServiceHeroes, result.data)
                }
                is Result.Error -> {
                    binding.serviceHeroesProgressBar.visibility = View.GONE
                    binding.recyclerViewServiceProviders.visibility = View.GONE
                    binding.tvEmptyServiceHeroes.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Failed to load service heroes: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Observe recent activities
        viewModel.recentActivities.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.recentActivitiesProgressBar.visibility = View.VISIBLE
                    binding.recyclerViewRecentActivity.visibility = View.GONE
                    binding.tvEmptyRecentActivities.visibility = View.GONE
                }
                is Result.Success -> {
                    binding.recentActivitiesProgressBar.visibility = View.GONE
                    binding.recyclerViewRecentActivity.visibility = View.VISIBLE
                    recentActivityAdapter.submitList(result.data)
                    toggleEmptyState(binding.recyclerViewRecentActivity, binding.tvEmptyRecentActivities, result.data)
                }
                is Result.Error -> {
                    binding.recentActivitiesProgressBar.visibility = View.GONE
                    binding.recyclerViewRecentActivity.visibility = View.GONE
                    binding.tvEmptyRecentActivities.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Failed to load recent activities: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private fun toggleEmptyState(recyclerView: RecyclerView, emptyView: View, data: List<Any>) {
        if (data.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the handler and binding
        autoSlider?.let { handler.removeCallbacks(it) }
        _binding = null
    }
}





