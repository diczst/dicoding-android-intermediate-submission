package com.neonusa.submission1.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.neonusa.submission1.core.data.source.model.Story
import com.neonusa.submission1.databinding.ItemStoryBinding
import com.neonusa.submission1.ui.detail.DetailActivity
import com.squareup.picasso.Picasso

class StoryListAdapter :
    PagingDataAdapter<Story, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Story) {
            binding.tvName.text = data.name
            binding.tvDescription.text = data.description

            Picasso.get()
                .load(data.photoUrl)
                .fit()
                .centerCrop()
                .into(binding.imgItem)

            itemView.setOnClickListener {
                val intent = Intent( itemView.context, DetailActivity::class.java)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.tvName, "name_detail"),
                        Pair(binding.tvDescription, "desc_detail"),
                        Pair(binding.imgItem, "img_detail")
                    )

                intent.putExtra(DetailActivity.EXTRA_STORY, data)
                itemView.context.startActivity(intent,optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}