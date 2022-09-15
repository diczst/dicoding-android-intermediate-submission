package com.neonusa.submission1.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.neonusa.submission1.R
import com.neonusa.submission1.adapter.StoryAdapter
import com.neonusa.submission1.core.data.source.remote.network.State
import com.neonusa.submission1.databinding.ActivityHomeBinding
import com.neonusa.submission1.ui.add.AddActivity
import com.neonusa.submission1.ui.login.LoginActivity
import com.neonusa.submission1.utils.UserPreference
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var adapter = StoryAdapter()
    private val viewModel: HomeViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        getData()
        setupAdapter()
    }

    private fun setupAdapter() {
        binding.rvStory.adapter = adapter
    }

    private fun getData() {
        viewModel.stories().observe(this) {
            when (it.state) {
                State.SUCCESS -> {
                    val data = it.data ?: emptyList()
                    Log.i("HomeActivity", "getData: $data")
                    adapter.addItems(data)

//                    binding.actionLogout.text = adapter.stories.size.toString()

                    if (data.isEmpty()) {
                    }
                }
                State.ERROR -> {
                    // show error
                    Log.i("StoreAddressActivity", "getData: ${it.message}")

                }
                State.LOADING -> {
                    // show loading
                }
            }
        }
    }


    private fun setupToolbar() {
        binding.toolbarHome.toolbarMain.apply {
            inflateMenu(R.menu.main_menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_logout -> {
                        UserPreference.isLogin = false
                        val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    R.id.menu_add -> {
                        val intent = Intent(this@HomeActivity, AddActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                false
            }
        }
    }
}