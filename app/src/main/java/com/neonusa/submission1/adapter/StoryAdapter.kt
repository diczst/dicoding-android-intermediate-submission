package com.neonusa.submission1.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.neonusa.submission1.core.data.source.model.Story
import com.neonusa.submission1.databinding.ItemStoryBinding
import com.neonusa.submission1.ui.detail.DetailActivity
import com.squareup.picasso.Picasso

@SuppressLint("NotifyDataSetChanged")
class StoryAdapter : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {
    private var stories = ArrayList<Story>()

    inner class ViewHolder(private val itemBinding: ItemStoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Story, position: Int) {
            itemBinding.apply {
                tvName.text = item.name
                tvDescription.text = item.description
                Picasso.get()
                    .load(item.photoUrl)
                    .fit()
                    .centerCrop()
                    .into(imgItem)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        androidx.core.util.Pair(tvName, "name_detail"),
                        androidx.core.util.Pair(tvDescription, "desc_detail"),
                        androidx.core.util.Pair(imgItem, "img_detail")
                    )

                itemView.setOnClickListener {
                    val intent = Intent( itemView.context, DetailActivity::class.java)
                    val story = stories[position]
                    intent.putExtra(DetailActivity.EXTRA_STORY, story)
                    itemView.context.startActivity(intent,optionsCompat.toBundle())
                }

            }
        }
    }

    fun addItems(items: List<Story>) {
        stories.clear()
        stories.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stories[position], position)
    }

    override fun getItemCount(): Int {
        return stories.size
    }

}