package com.neonusa.submission1.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.neonusa.submission1.R
import com.neonusa.submission1.adapter.StoryAdapter
import com.neonusa.submission1.core.data.source.remote.network.State
import com.neonusa.submission1.databinding.ActivityHomeBinding
import com.neonusa.submission1.ui.add.AddActivity
import com.neonusa.submission1.ui.login.LoginActivity
import com.neonusa.submission1.ui.map.MapsActivity
import com.neonusa.submission1.utils.UserPreference
import com.techiness.progressdialoglibrary.ProgressDialog
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var adapter = StoryAdapter()
    private val viewModel: HomeViewModel by inject()
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)

        setupToolbar()
        getData()
        setupAdapter()
    }

    override fun onResume() {
        getData()
        super.onResume()
    }

    private fun setupAdapter() {
        binding.rvStory.adapter = adapter
    }

    private fun getData() {
        viewModel.stories(UserPreference.getUserToken().toString()).observe(this) {
            when (it.state) {
                State.SUCCESS -> {
                    progressDialog.dismiss()
                    val data = it.data ?: emptyList()
                    adapter.addItems(data)
                }
                State.ERROR -> {
                    progressDialog.dismiss()
                    Toast.makeText(this, getString(R.string.fail_get_data), Toast.LENGTH_SHORT).show()
                }
                State.LOADING -> {
                    progressDialog.show()
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