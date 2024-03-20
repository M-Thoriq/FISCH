package com.harissabil.fisch.ui.history

import androidx.lifecycle.ViewModel
import com.harissabil.fisch.data.FishRepository

class HistoryViewModel(private val repository: FishRepository) : ViewModel() {

    fun getAllFish() = repository.getAllFish()

}