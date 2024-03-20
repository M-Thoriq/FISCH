package com.harissabil.fisch.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harissabil.fisch.data.FishRepository
import com.harissabil.fisch.data.local.entity.FishEntity
import kotlinx.coroutines.launch

class AddViewModel(private val repository: FishRepository) : ViewModel() {

    fun insert(fish: FishEntity) {
        viewModelScope.launch {
            repository.insert(fish)
        }
    }

    fun update(fish: FishEntity) {
        viewModelScope.launch {
            repository.update(fish)
        }
    }

    fun delete(fish: FishEntity) {
        viewModelScope.launch {
            repository.delete(fish)
        }
    }

}