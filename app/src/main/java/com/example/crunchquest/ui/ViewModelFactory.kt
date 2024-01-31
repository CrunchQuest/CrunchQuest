package com.example.crunchquest.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.crunchquest.data.QuestRepository
import com.example.crunchquest.ui.screen.detail.DetailViewModel
import com.example.crunchquest.ui.screen.home.HomeViewModel
import com.example.crunchquest.ui.screen.track.TrackViewModel

//class ViewModelFactory(private val repository: QuestRepository) :
//    ViewModelProvider.NewInstanceFactory() {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
//            return HomeViewModel(repository) as T
//        } else if (modelClass.isAssignableFrom(TrackViewModel::class.java)) {
//            return TrackViewModel(repository) as T
//        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
//            return DetailViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
//    }
//}