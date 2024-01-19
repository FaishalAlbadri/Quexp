package com.bintang.quexp.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.data.local.UserData
import com.bintang.quexp.data.user.UserItem
import com.bintang.quexp.data.user.UserResponse
import com.bintang.quexp.util.UserPreferences
import com.bintang.quexp.util.viewmodel.Event
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userResponse = MutableLiveData<UserItem>()
    val userResponse: LiveData<UserItem> = _userResponse

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun login(email: String, password: String) {
        _isLoading.value = true
        val client = APIConfig.build().userLogin(email, password)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    val userResponse = response.body()
                    if (userResponse!!.message.equals("Login berhasil")) {
                        _userResponse.value = userResponse.userItem!!
                    } else {
                        _message.value = Event(userResponse.message!!)
                    }
                } else {
                    _message.value = Event(response.message().toString())
                    Log.e(
                        TAG,
                        "Failure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })

    }

    fun saveSession(userData: UserData) {
        viewModelScope.launch {
            userPreferences.saveSession(userData)
        }
    }
}