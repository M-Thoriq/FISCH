package com.harissabil.fisch.ui.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harissabil.fisch.data.local.entity.FishEntity
import com.harissabil.fisch.databinding.ItemHistoryBinding
import com.harissabil.fisch.ui.add.AddActivity

class HistoryListAdapter(private val historyList: List<FishEntity>) : RecyclerView.Adapter<HistoryListAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = historyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = historyList[position]
        holder.binding.apply {
            tvTanggal.text = list.date
            tvLocationToday.text = list.location
            tvHargaToday.text = "Rp ${list.price}"
            tvBeratToday.text = "${list.weight} Kg"
        }

        holder.binding.cvHistory.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddActivity::class.java)
            intent.putExtra(AddActivity.EXTRA_TYPE, "edit")
            intent.putExtra(AddActivity.EXTRA_FISH, list)
            holder.itemView.context.startActivity(intent)
        }
    }
}
