package com.android.example.architecture_basics.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.example.architecture_basics.databinding.RowLoginItemBinding
import com.android.example.architecture_basics.ui.views.Item

class LoginAdapter(val itemClick: (position: Int) -> Unit) :
    RecyclerView.Adapter<LoginAdapter.LoginViewHolder>() {

    private var items: List<Item> = listOf()

    inner class LoginViewHolder(private var binding: RowLoginItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.listItemText.text = item.title
            binding.listItemIcon.setImageResource(item.icon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoginViewHolder {
        return LoginViewHolder(
            RowLoginItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LoginViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            itemClick(position)
        }
    }

    override fun getItemCount() = items.size

    fun setItems(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }
}

