package com.example.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.remote.response.ItemsItem

class UserAdapter(private val listUser: List<ItemsItem>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback?=null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(listUser: ItemsItem)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false))

    override fun getItemCount() = listUser.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view.findViewById(R.id.img_item_avatar)
        val tvItem: TextView = view.findViewById(R.id.tv_item_login)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val user = listUser[position]
        viewHolder.tvItem.text = user.login
        Glide.with(viewHolder.itemView)
            .load(user.avatarUrl)
            .into(viewHolder.imageView)
        viewHolder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(listUser[viewHolder.adapterPosition])
        }
    }
}