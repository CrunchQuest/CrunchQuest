package com.crunchquest.android.presentation.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crunchquest.android.domain.entities.Provider
import com.crunchquest.shared.databinding.ItemProviderBinding

class ProviderAdapter : ListAdapter<Provider, ProviderAdapter.ProviderViewHolder>(ProviderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProviderViewHolder {
        val binding = ItemProviderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProviderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProviderViewHolder, position: Int) {
        val provider = getItem(position)
        holder.bind(provider)
    }

    class ProviderViewHolder(private val binding: ItemProviderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(provider: Provider) {
            binding.apply {
                providerName.text = provider.providerId
                // Add accessibility content description
                providerName.contentDescription = "Provider Name: ${provider.providerId}"

                // Example of additional setup or click listeners
                root.setOnClickListener {
                    // Handle click event
                }
            }
        }
    }
}

class ProviderDiffCallback : DiffUtil.ItemCallback<Provider>() {
    override fun areItemsTheSame(oldItem: Provider, newItem: Provider): Boolean {
        return oldItem.providerId == newItem.providerId
    }

    override fun areContentsTheSame(oldItem: Provider, newItem: Provider): Boolean {
        return oldItem == newItem
    }
}
