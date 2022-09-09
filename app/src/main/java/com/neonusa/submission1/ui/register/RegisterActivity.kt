package com.neonusa.submission1.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.neonusa.submission1.core.data.source.remote.network.State
import com.neonusa.submission1.core.data.source.remote.request.RegisterRequest
import com.neonusa.submission1.databinding.ActivityRegisterBinding
import com.neonusa.submission1.ui.home.HomeActivity
import com.neonusa.submission1.ui.login.LoginActivity
import org.koin.android.ext.android.inject

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val pass = binding.edRegisterPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && name.isNotEmpty()) {
                if(pass.length >= 6) {
                    val body = RegisterRequest(
                        name,
                        email,
                        pass
                    )
                    
                    viewModel.register(body).observe(this) {
                        when(it.state){
                            State.SUCCESS -> {
                                Toast.makeText(this, "Berhasil melakukan pendaftaran, Silahkan Login", Toast.LENGTH_LONG).show()
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            State.ERROR -> {
                                binding.pbarRegister.visibility = View.GONE
                                binding.layoutRegister.visibility = View.VISIBLE
                            }
                            State.LOADING -> {
                                binding.pbarRegister.visibility = View.VISIBLE
                                binding.layoutRegister.visibility = View.GONE
                            }
                        }

                    }



                } else {
                    binding.edRegisterPassword.requestFocus()
                    binding.edRegisterPassword.setError("Password tidak boleh kurang dari 6 karakter", null)
                }

            } else {
                if(email.isEmpty()){
                    binding.edRegisterEmail.error = "Email tidak boleh kosong"
                }

                if(pass.isEmpty()) {
                    binding.edRegisterPassword.requestFocus()
                    binding.edRegisterPassword.setError("Password tidak boleh kosong", null)
                }

                if(name.isEmpty()){
                    binding.edRegisterName.error = "Nama tidak boleh kosong"
                }
            }
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}