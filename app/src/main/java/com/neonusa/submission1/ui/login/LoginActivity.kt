package com.neonusa.submission1.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.neonusa.submission1.core.data.source.remote.network.State
import com.neonusa.submission1.core.data.source.remote.request.LoginRequest
import com.neonusa.submission1.databinding.ActivityLoginBinding
import com.neonusa.submission1.ui.home.HomeActivity
import com.neonusa.submission1.ui.register.RegisterActivity
import com.neonusa.submission1.utils.UserPreference
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val pass = binding.edLoginPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                if(pass.length >= 6) {

                    val body = LoginRequest(
                        binding.edLoginEmail.text.toString(),
                        binding.edLoginPassword.text.toString()
                    )

                    viewModel.login(body).observe(this) {
                        when(it.state){
                            State.SUCCESS -> {
//                                dismisLoading()
                                Toast.makeText(this, "Selamat datang : ${it.data?.name}", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            State.ERROR -> {
//                                dismisLoading()
                                Toast.makeText(this, it.message ?: "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                            }
                            State.LOADING -> {
//                                showLoading()
                            }
                        }

                    }

                } else {
                    binding.edLoginPassword.requestFocus()
                    binding.edLoginPassword.setError("Password tidak boleh kurang dari 6 karakter", null)
                }

            } else {
                if(email.isEmpty()){
                    binding.edLoginEmail.error = "Email tidak boleh kosong"
                } else if(pass.isEmpty()) {
                    binding.edLoginPassword.requestFocus()
                    binding.edLoginPassword.setError("Password tidak boleh kosong", null)
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        if(UserPreference.isLogin){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}