package com.android.example.architecture_basics.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.example.architecture_basics.R
import com.android.example.architecture_basics.data.network.models.BeerApi
import com.android.example.architecture_basics.databinding.RowBeerItemBinding

class BeersAdapter: ListAdapter<BeerApi, BeersAdapter.BeerViewHolder>(DiffCallback) {

    class BeerViewHolder(private var binding: RowBeerItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(beer: BeerApi) {
            //Set imagen
            val imgUri = beer.imageUrl!!.toUri().buildUpon().scheme("https").build()
            binding.ivImagen.load(imgUri) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
            }
            binding.txtName.text = beer.name
            binding.txtTag.text = beer.tagline
            binding.txtFirsBrewed.text = beer.firstBrewed
            binding.txtIbu.text = beer.ibu.toString()
            binding.txtAbv.text = "${beer.abv}%"

        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<BeerApi>() {
            override fun areItemsTheSame(oldItem: BeerApi, newItem: BeerApi): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: BeerApi, newItem: BeerApi): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        val viewHolder = BeerViewHolder(
            RowBeerItemBinding.inflate(
                LayoutInflater.from( parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            //onItemClicked(getItem(position))
        }
        return viewHolder
    }


    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}