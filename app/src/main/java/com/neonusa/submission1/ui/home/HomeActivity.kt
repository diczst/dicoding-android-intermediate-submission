package com.neonusa.submission1.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.neonusa.submission1.R
import com.neonusa.submission1.adapter.LoadingStateAdapter
import com.neonusa.submission1.adapter.StoryListAdapter
import com.neonusa.submission1.databinding.ActivityHomeBinding
import com.neonusa.submission1.ui.add.AddActivity
import com.neonusa.submission1.ui.login.LoginActivity
import com.neonusa.submission1.ui.map.MapsActivity
import com.neonusa.submission1.utils.UserPreference
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        getData()
    }


    private fun getData(){
        val adapter = StoryListAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.paginatedStories.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun setupToolbar() {
        binding.toolbarHome.toolbarMain.apply {
            inflateMenu(R.menu.main_menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_logout -> {
                        UserPreference.isLogin = false
                        UserPreference.token = ""
                        val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    R.id.menu_add -> {
                        val intent = Intent(this@HomeActivity, AddActivity::class.java)
                        startActivity(intent)
                    }
                    R.id.menu_map -> {
                        val intent =  Intent(this@HomeActivity, MapsActivity::class.java)
                        startActivity(intent)
                    }
                }
                false
            }
        }
    }
}