package com.neonusa.submission1.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.neonusa.submission1.R
import com.neonusa.submission1.databinding.ActivityHomeBinding
import com.neonusa.submission1.ui.login.LoginActivity
import com.neonusa.submission1.utils.UserPreference

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.actionLogout.setOnClickListener {
            UserPreference.isLogin = false
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}