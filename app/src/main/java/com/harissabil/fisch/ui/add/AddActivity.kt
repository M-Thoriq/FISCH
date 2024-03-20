package com.harissabil.fisch.ui.add

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.harissabil.fisch.data.local.entity.FishEntity
import com.harissabil.fisch.databinding.ActivityAddBinding
import com.harissabil.fisch.ui.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding

    private val addViewModel by viewModels<AddViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val type = intent.getStringExtra(EXTRA_TYPE)
        val fishData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_FISH, FishEntity::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_FISH)
        }

        setupView(type, fishData)
        setupAction(type, fishData)
    }

    private fun setupView(type: String?, fishData: FishEntity?) {
        if (type == TYPE_EDIT) {
            binding.apply {
                tvTitle.text = "Edit Ikan"
                etLocation.setText(fishData?.location)
                etDate.setText(fishData?.date)
                etPrice.setText(fishData?.price.toString())
                etWeight.setText(fishData?.weight.toString())
                btnInfo.text = "Update"
                btnDelete.visibility = View.VISIBLE
            }
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupAction(type: String?, fishData: FishEntity?) {
        binding.apply {
            btnInfo.setOnClickListener {
                if (etLocation.text.isNullOrEmpty()) {
                    tilLocation.error = "Location cannot be empty"
                } else if (etDate.text.isNullOrEmpty()) {
                    tilDate.error = "Date cannot be empty"
                } else if (etPrice.text.isNullOrEmpty()) {
                    tilPrice.error = "Price cannot be empty"
                } else if (etWeight.text.isNullOrEmpty()) {
                    tilWeight.error = "Weight cannot be empty"
                } else {
                    val fish = FishEntity(
                        location = etLocation.text.toString(),
                        date = etDate.text.toString(),
                        price = etPrice.text.toString().toInt(),
                        weight = etWeight.text.toString().toInt(),
                    )
                    if (type == TYPE_EDIT) {
                        fish.id = fishData!!.id
                        addViewModel.update(fish)
                    } else {
                        addViewModel.insert(fish)
                    }
                    finish()
                }
            }

            btnDelete.setOnClickListener {
                val builder = MaterialAlertDialogBuilder(this@AddActivity)
                builder.setTitle("Hapus Data?")
                builder.setMessage("Data ikan akan dihapus secara permanen.")
                builder.setPositiveButton("Hapus") { _, _ ->
                    addViewModel.delete(fishData!!)
                    Snackbar.make(binding.root, "Data dihapus", Snackbar.LENGTH_SHORT).show()
                    finish()
                }
                builder.setNegativeButton("Batal") { _, _ ->
                }
                builder.show()
            }
        }
        binding.etDate.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                datePicker()
            }
        }
        binding.etDate.setOnClickListener {
            datePicker()
        }
    }

    @SuppressLint("SetTextI18n")
    fun datePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setTextInputFormat(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()))
                .build()
        datePicker.show(supportFragmentManager, "date_picker")
        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.time = Date(it)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.format(calendar.time)
            binding.etDate.setText(
                date
            )
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

    companion object {
        const val EXTRA_TYPE = "extra_type"
        const val TYPE_EDIT = "edit"
        const val EXTRA_FISH = "extra_fish"
    }
}