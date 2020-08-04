package com.lukasstancikas.amrotestvenues.feature.venuelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lukasstancikas.amrotestvenues.databinding.ItemVenueBinding
import com.lukasstancikas.amrotestvenues.model.Venue

class VenueAdapter : ListAdapter<Venue, VenueAdapter.MyViewHolder>(CharacterDiffCallback()) {

    private var itemClick: ((Venue) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemBinding =
            ItemVenueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(item, itemClick)

        }
    }

    fun onItemClick(itemClick: ((Venue) -> Unit)?) {
        this.itemClick = itemClick
    }

    class MyViewHolder(private val itemBinding: ItemVenueBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: Venue, itemClick: ((Venue) -> Unit)?) {
            itemBinding.itemName.text = item.name
            // TODO more location handling
            itemBinding.itemLocation.text = item.location.address
            itemBinding.root.setOnClickListener {
                itemClick?.invoke(item)
            }
        }
    }

    private class CharacterDiffCallback : DiffUtil.ItemCallback<Venue>() {

        override fun areItemsTheSame(oldItem: Venue, newItem: Venue): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Venue, newItem: Venue): Boolean {
            return oldItem.name == newItem.name && oldItem.location == newItem.location
        }
    }
}