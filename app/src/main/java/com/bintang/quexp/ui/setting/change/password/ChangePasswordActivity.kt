package com.bintang.quexp.ui.setting.change.password

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.bintang.quexp.R
import com.bintang.quexp.databinding.ActivityChangePasswordBinding
import com.bintang.quexp.util.createAlertDialog
import com.bintang.quexp.util.viewmodel.ViewModelFactory

class ChangePasswordActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private var _binding: ActivityChangePasswordBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val changePasswordViewModel: ChangePasswordViewModel by viewModels { viewModel }
    private lateinit var loading: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelFactory.getInstance(this)

        createLoading()

        binding.apply {
            btnBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }

            btnSubmit.setOnClickListener {
                if (edtPassword.length() == 0 || edtNewPassword.length() < 8 || edtRepeatNewPassword.length() < 8) {
                    if (edtPassword.length() == 0) {
                        edtPassword.error = getString(R.string.error_field)
                    }
                    if (edtNewPassword.length() < 8) {
                        edtNewPassword.error = getString(R.string.error_field)
                    }
                    if (edtRepeatNewPassword.length() < 8) {
                        edtRepeatNewPassword.error = getString(R.string.error_field)
                    }
                } else {
                    if (edtNewPassword.text.toString().equals(edtRepeatNewPassword.text.toString())) {
                        changePasswordViewModel.changePassword(edtNewPassword.text.toString(), edtPassword.text.toString())
                    } else {
                        edtRepeatNewPassword.error = "Password tidak sesuai"
                    }
                }
            }
        }

        changePasswordViewModel.apply {
            isLoading.observe(this@ChangePasswordActivity) {
                showLoading(it)
            }
            message.observe(this@ChangePasswordActivity) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(this@ChangePasswordActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
            userResponse.observe(this@ChangePasswordActivity) {
                Toast.makeText(this@ChangePasswordActivity, "Berhasil ubah kata sandi", Toast.LENGTH_SHORT).show()
                onBackPressedCallback.handleOnBackPressed()
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