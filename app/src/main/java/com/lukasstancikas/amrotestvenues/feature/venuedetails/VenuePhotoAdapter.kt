package com.lukasstancikas.amrotestvenues.feature.venuedetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.with
import com.lukasstancikas.amrotestvenues.databinding.ItemVenuePhotoBinding
import com.lukasstancikas.amrotestvenues.model.VenuePhoto

class VenuePhotoAdapter :
    ListAdapter<VenuePhoto, VenuePhotoAdapter.MyViewHolder>(CharacterDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemBinding =
            ItemVenuePhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(item)
        }
    }

    class MyViewHolder(private val itemBinding: ItemVenuePhotoBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: VenuePhoto) {
            with(itemBinding.root.context)
                .load(item.toString())
                .into(itemBinding.imageView)
        }
    }

    private class CharacterDiffCallback : DiffUtil.ItemCallback<VenuePhoto>() {
        override fun areItemsTheSame(oldItem: VenuePhoto, newItem: VenuePhoto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VenuePhoto, newItem: VenuePhoto): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }
}