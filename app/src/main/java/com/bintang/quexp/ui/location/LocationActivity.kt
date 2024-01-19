package com.bintang.quexp.ui.location

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bintang.quexp.databinding.ActivityLocationBinding
import com.bintang.quexp.ui.login.LoginActivity

class LocationActivity : AppCompatActivity() {

    private var _binding: ActivityLocationBinding? = null
    val binding get() = _binding!!

    companion object {
        val REQUEST_CODE = 1234
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnLocation.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        this@LocationActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                } else {
                    ActivityCompat.requestPermissions(
                        this@LocationActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_CODE
                    )
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Gagal menambahkan izin lokasi!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}