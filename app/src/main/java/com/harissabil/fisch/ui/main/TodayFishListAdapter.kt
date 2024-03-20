package com.harissabil.fisch.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harissabil.fisch.data.local.entity.FishEntity
import com.harissabil.fisch.databinding.ItemTodayBinding
import com.harissabil.fisch.ui.add.AddActivity

class TodayFishListAdapter(private val todayList: List<FishEntity>) : RecyclerView.Adapter<TodayFishListAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemTodayBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTodayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = todayList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = todayList[position]
        holder.binding.apply {
            tvLocationToday.text = list.location
            tvHargaToday.text = "Rp. ${list.price}"
            tvBeratToday.text = "${list.weight} Kg"
        }

        holder.binding.cvToday.setOnClickListener {
            val intent = Intent(holder.itemView.context, AddActivity::class.java)
            intent.putExtra(AddActivity.EXTRA_TYPE, "edit")
            intent.putExtra(AddActivity.EXTRA_FISH, list)
            holder.itemView.context.startActivity(intent)
        }
    }
}