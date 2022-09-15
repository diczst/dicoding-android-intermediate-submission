package com.neonusa.submission1.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
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
                                Toast.makeText(this, "Selamat datang : ${it.data?.name}", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            State.ERROR -> {
                                binding.pbarLogin.visibility = View.GONE
                                binding.layoutLogin.visibility = View.VISIBLE

                                MaterialDialog(this).show {
                                    title(text = "Login Gagal")
                                    message(text = "${it.message}")
                                    negativeButton(text = "Coba Lagi") { materialDialog ->
                                        materialDialog.dismiss()
                                    }
                                }
                            }
                            State.LOADING -> {
                                binding.layoutLogin.visibility = View.GONE
                                binding.pbarLogin.visibility = View.VISIBLE
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