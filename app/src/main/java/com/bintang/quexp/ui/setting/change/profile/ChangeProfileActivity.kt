package com.bintang.quexp.ui.setting.change.profile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.bintang.quexp.R
import com.bintang.quexp.databinding.ActivityChangeProfileBinding
import com.bintang.quexp.util.createAlertDialog
import com.bintang.quexp.util.loadCircularImage
import com.bintang.quexp.util.loadCircularImageURI
import com.bintang.quexp.util.reduceFileImage
import com.bintang.quexp.util.rotateFile
import com.bintang.quexp.util.uriToFile
import com.bintang.quexp.util.viewmodel.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ChangeProfileActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private var _binding: ActivityChangeProfileBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val changeProfileViewModel: ChangeProfileViewModel by viewModels { viewModel }
    private lateinit var loading: AlertDialog
    private var getFile: File? = null

    private val map: MutableMap<String, RequestBody> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivityChangeProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelFactory.getInstance(this)

        createLoading()

        binding.apply {
            imgProfile.setOnClickListener {
                openGallery()
            }
            btnBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }
            imgProfile.loadCircularImage(getDataIntent("user_img"), 8F)
            edtUsername.setText(getDataIntent("user_name"))
            edtEmail.apply {
                setEnabled(false)
                setText(getDataIntent("user_email"))
            }
            edtPhone.setText(getDataIntent("user_phone"))
            edtCity.setText(getDataIntent("user_city"))

            btnSubmit.setOnClickListener {
                if (edtUsername.length() < 0 || edtPhone.length() < 8 || edtCity.length() < 4) {
                    if (edtUsername.length() == 0) {
                        edtUsername.error = getString(R.string.error_field)
                    }
                    if (edtPhone.length() == 0) {
                        edtPhone.error = getString(R.string.error_field)
                    }
                    if (edtCity.length() == 0) {
                        edtCity.error = getString(R.string.error_field)
                    }
                } else {
                    if (getFile == null && edtUsername.text.toString()
                            .equals(getDataIntent("user_name")) && edtEmail.text.toString()
                            .equals(getDataIntent("user_email")) && edtPhone.text.toString()
                            .equals(getDataIntent("user_phone")) && edtCity.text.toString()
                            .equals(getDataIntent("user_city"))
                    ) {
                        Toast.makeText(
                            this@ChangeProfileActivity,
                            "Tidak ada data yang berubah",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (getFile != null) {
                            try {
                                val file = getFile as File
                                map.put(
                                    "user_img\"; filename=\"" + file.getName() + "\"",
                                    file.asRequestBody("image/*".toMediaType())
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        map.put(
                            "user_name",
                            edtUsername.text.toString().toRequestBody("text/plain".toMediaType())
                        )
                        map.put(
                            "user_email",
                            edtEmail.text.toString().toRequestBody("text/plain".toMediaType())
                        )
                        map.put(
                            "user_phone",
                            edtPhone.text.toString().toRequestBody("text/plain".toMediaType())
                        )
                        map.put(
                            "user_city",
                            edtCity.text.toString().toRequestBody("text/plain".toMediaType())
                        )
                        changeProfileViewModel.changeProfile(map)
                    }
                }
            }

            changeProfileViewModel.apply {
                isLoading.observe(this@ChangeProfileActivity) {
                    showLoading(it)
                }
                message.observe(this@ChangeProfileActivity) {
                    it.getContentIfNotHandled()?.let {
                        Toast.makeText(this@ChangeProfileActivity, it, Toast.LENGTH_SHORT).show()
                    }
                }
                userResponse.observe(this@ChangeProfileActivity) {
                    Toast.makeText(
                        this@ChangeProfileActivity,
                        "Berhasil ubah profile",
                        Toast.LENGTH_SHORT
                    ).show()
                    onBackPressedCallback.handleOnBackPressed()
                }
            }
        }
    }

    private fun openGallery() {
        val chooser = Intent.createChooser(
            Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"),
            "Choose a Picture"
        )
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)

            myFile.let {file ->
                rotateFile(file)
                reduceFileImage(file)
                getFile = file
            }

            binding.imgProfile.loadCircularImageURI(myFile, 8F)
        }
    }

    private fun createLoading() {
        loading = createAlertDialog(this)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) loading.show() else loading.dismiss()
    }

    fun getDataIntent(string: String): String {
        return intent.getStringExtra(string).toString()
    }
}