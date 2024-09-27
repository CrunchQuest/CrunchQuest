package com.crunchquest.android.presentation.ui.activity

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crunchquest.shared.databinding.ItemActivityTypeBinding

class ActivityTypeAdapter(
    private val types: List<ActivityType>,
    private val selectedType: ActivityType,
    private val onTypeSelected: (ActivityType) -> Unit
) : RecyclerView.Adapter<ActivityTypeAdapter.TypeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val binding = ItemActivityTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        val type = types[position]
        holder.bind(type, type == selectedType)
    }

    override fun getItemCount(): Int = types.size

    inner class TypeViewHolder(private val binding: ItemActivityTypeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(type: ActivityType, isSelected: Boolean) {
            binding.typeName.text = type.name
            binding.typeName.setTextColor(if (isSelected) Color.BLUE else Color.BLACK)// Highlight selected
            itemView.setOnClickListener { onTypeSelected(type) }
        }
    }
}
