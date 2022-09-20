package com.neonusa.submission1.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.neonusa.submission1.core.data.source.model.Story
import com.neonusa.submission1.databinding.ActivityDetailBinding
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_STORY = "EXTRA_STORY"
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val story = intent.getParcelableExtra<Story>(EXTRA_STORY) as Story

        binding.tvDetailName.text = story.name
        binding.tvDetailDescription.text = story.description

        Picasso.get()
            .load(story.photoUrl)
            .fit()
            .centerCrop()
            .into(binding.ivDetailPhoto)
    }
}