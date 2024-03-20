package com.harissabil.fisch.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harissabil.fisch.data.FishRepository
import com.harissabil.fisch.di.Injection
import com.harissabil.fisch.ui.add.AddViewModel
import com.harissabil.fisch.ui.history.HistoryViewModel
import com.harissabil.fisch.ui.main.MainViewModel

class ViewModelFactory constructor(private val repository: FishRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(this.repository) as T
        } else if (modelClass.isAssignableFrom(AddViewModel::class.java)) {
            AddViewModel(this.repository) as T
        } else if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            HistoryViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}