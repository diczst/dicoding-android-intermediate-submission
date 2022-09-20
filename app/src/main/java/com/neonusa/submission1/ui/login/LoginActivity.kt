package com.neonusa.submission1.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.neonusa.submission1.R
import com.neonusa.submission1.core.data.source.remote.network.State
import com.neonusa.submission1.core.data.source.remote.request.LoginRequest
import com.neonusa.submission1.databinding.ActivityLoginBinding
import com.neonusa.submission1.ui.home.HomeActivity
import com.neonusa.submission1.ui.register.RegisterActivity
import com.neonusa.submission1.utils.UserPreference
import com.techiness.progressdialoglibrary.ProgressDialog
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by inject()
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)

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
                                progressDialog.dismiss()
                                Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            State.ERROR -> {
                                progressDialog.dismiss()
                                binding.pbarLogin.visibility = View.GONE
                                binding.layoutLogin.visibility = View.VISIBLE

                                MaterialDialog(this).show {
                                    title(text = getString(R.string.login_fail))
                                    message(text = "${it.message}")
                                    negativeButton(text = getString(R.string.try_again)) { materialDialog ->
                                        materialDialog.dismiss()
                                    }
                                }
                            }
                            State.LOADING -> {
                                progressDialog.show()
                                binding.layoutLogin.visibility = View.GONE
                                binding.pbarLogin.visibility = View.VISIBLE
                            }
                        }

                    }

                }

            } else {
                if(email.isEmpty()){
                    binding.edLoginEmail.error = getString(R.string.email_required)
                }
                if(pass.isEmpty()) {
                    binding.edLoginPassword.requestFocus()
                    binding.edLoginPassword.setError(getString(R.string.password_required), null)
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