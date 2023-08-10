package com.example.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.inventory.data.Item
import com.example.inventory.data.getFormattedPrice
import com.example.inventory.databinding.ItemListItemBinding

class ItemListAdapter(private val onItemClickListener: (Item) -> Unit) : ListAdapter<Item, ItemListAdapter.ItemViewHolder>(DiffCallback) {

    inner class ItemViewHolder(private val binding: ItemListItemBinding) :
        ViewHolder(binding.root) {
        fun bind(item: Item) = binding.apply {
            itemName.text = item.name
            itemPrice.text = item.getFormattedPrice()
            itemQuantity.text = item.quantity.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemListItemBinding.inflate(
            LayoutInflater.from(parent.context)
        )
        return ItemViewHolder(binding).apply {
            itemView.setOnClickListener {
                val position = adapterPosition
                onItemClickListener(getItem(position))
            }
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem === newItem

            override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
        }
    }
}