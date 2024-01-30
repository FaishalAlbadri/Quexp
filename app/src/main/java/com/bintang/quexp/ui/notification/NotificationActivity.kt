package com.bintang.quexp.ui.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintang.quexp.adapter.NotificationBaseAdapter
import com.bintang.quexp.data.notification.NotificationTimeItem
import com.bintang.quexp.databinding.ActivityNotificationBinding
import com.bintang.quexp.util.viewmodel.ViewModelFactory

class NotificationActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private var _binding: ActivityNotificationBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val notificationViewModel: NotificationViewModel by viewModels { viewModel }
    private lateinit var notificationBaseAdapter: NotificationBaseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelFactory.getInstance(this)

        binding.apply {
            binding.toolbarNotification.apply {
                txtTitleBar.text = "Notification"
                btnBack.setOnClickListener {
                    onBackPressedCallback.handleOnBackPressed()
                }
            }
        }

        notificationViewModel.apply {
            notificationResponse.observe(this@NotificationActivity){
                setNotif(it)
            }
            isLoading.observe(this@NotificationActivity) {
                showLoading(it)
            }
            message.observe(this@NotificationActivity) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(this@NotificationActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
        notificationViewModel.notification()
    }

    private fun setNotif(it: List<NotificationTimeItem>) {
        notificationBaseAdapter = NotificationBaseAdapter(it)
        binding.rvNotification.apply {
            layoutManager = LinearLayoutManager(this@NotificationActivity)
            adapter = notificationBaseAdapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.apply {
                loading.root.visibility = View.VISIBLE
            }
        } else {
            binding.apply {
                loading.root.visibility = View.GONE
            }
        }
    }

}