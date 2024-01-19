package com.bintang.quexp.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.bintang.quexp.R
import com.bintang.quexp.databinding.ActivityRegisterBinding
import com.bintang.quexp.ui.login.LoginActivity
import com.bintang.quexp.util.htmlStringFormat

class RegisterActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private var _binding: ActivityRegisterBinding? = null
    val binding get() = _binding!!
    private val registerViewModel by viewModels<RegisterViewModel>()
    private lateinit var loading: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createLoading()

        binding.apply {
            btnLogin.apply {
                text = htmlStringFormat(this@RegisterActivity, "Sudah memiliki akun?", "Masuk")
                setOnClickListener {
                    onBackPressedCallback.handleOnBackPressed()
                }
            }

            btnRegister.setOnClickListener {
                if (edtEmail.length() == 0 || edtPassword.length() < 8 || edtRepeatPassword.length() < 8 || edtRepeatPassword.text.toString() != edtPassword.text.toString()) {
                    if (edtEmail.length() == 0) {
                        edtEmail.error = getString(R.string.error_field)
                    }
                    if (edtPassword.length() < 8) {
                        edtPassword.error = getString(R.string.error_field)
                    }
                    if (edtRepeatPassword.length() < 8) {
                        edtRepeatPassword.error = getString(R.string.error_field)
                    } else {
                        if (edtRepeatPassword.text.toString() != edtPassword.text.toString()) {
                            edtRepeatPassword.error = getString(R.string.error_repeat_password)
                        }
                    }
                } else {
                    registerViewModel.register(edtEmail.text.toString(), edtPassword.text.toString())
                }
            }
        }

        registerViewModel.apply {
            isLoading.observe(this@RegisterActivity) {
                showLoading(it)
            }
            message.observe(this@RegisterActivity) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(this@RegisterActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
            userResponse.observe(this@RegisterActivity) {
                onBackPressedCallback.handleOnBackPressed()
            }
        }
    }

    private fun createLoading() {
        loading = AlertDialog.Builder(this)
            .setCancelable(false)
            .setView(R.layout.loading)
            .create()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) loading.show() else loading.dismiss()
    }
}