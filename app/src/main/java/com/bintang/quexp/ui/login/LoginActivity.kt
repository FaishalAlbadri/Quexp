package com.bintang.quexp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.bintang.quexp.R
import com.bintang.quexp.data.local.UserData
import com.bintang.quexp.databinding.ActivityLoginBinding
import com.bintang.quexp.ui.home.HomeActivity
import com.bintang.quexp.ui.register.RegisterActivity
import com.bintang.quexp.util.createAlertDialog
import com.bintang.quexp.util.htmlStringFormat
import com.bintang.quexp.util.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private var _binding: ActivityLoginBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { viewModel }
    private lateinit var loading: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelFactory.getInstance(this)

        createLoading()

        binding.apply {
            btnRegister.apply {
                text = htmlStringFormat(this@LoginActivity, "Belum punya akun?", "Daftar")
                setOnClickListener {
                    startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                }
            }

            btnLogin.setOnClickListener {
                if (edtEmail.length() == 0 || edtPassword.length() < 8) {
                    if (edtEmail.length() == 0) {
                        edtEmail.error = getString(R.string.error_field)
                    }
                    if (edtPassword.length() < 8) {
                        edtPassword.error = getString(R.string.error_field)
                    }
                } else {
                    loginViewModel.login(edtEmail.text.toString(), edtPassword.text.toString())
                }
            }
        }

        loginViewModel.apply {
            isLoading.observe(this@LoginActivity) {
                showLoading(it)
            }
            message.observe(this@LoginActivity) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(this@LoginActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
            userResponse.observe(this@LoginActivity) {
                saveSession(UserData(it.idUser, it.userName, it.userToken, true))
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                finish()

            }
        }
    }

    private fun createLoading() {
        loading = createAlertDialog(this)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) loading.show() else loading.dismiss()
    }
}