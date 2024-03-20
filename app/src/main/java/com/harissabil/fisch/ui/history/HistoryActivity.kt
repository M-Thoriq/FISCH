package com.harissabil.fisch.ui.history

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.harissabil.fisch.databinding.ActivityHistoryBinding
import com.harissabil.fisch.ui.ViewModelFactory

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    private val historyViewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
    }

    private fun setupView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.rvHistory.layoutManager = LinearLayoutManager(this)
    }

    private fun setupViewModel() {
        historyViewModel.getAllFish().observe(this) { fish ->
            if (fish.isNotEmpty()) {
                binding.rvHistory.adapter = HistoryListAdapter(fish)
                binding.tvNoHistory.visibility = View.GONE
            } else {
                binding.tvNoHistory.visibility = View.VISIBLE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}