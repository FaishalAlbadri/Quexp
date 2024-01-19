package com.bintang.quexp.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.data.BaseResponse
import com.bintang.quexp.util.viewmodel.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {

    companion object {
        private const val TAG = "RegisterViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userResponse = MutableLiveData<BaseResponse>()
    val userResponse: LiveData<BaseResponse> = _userResponse

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun register(email: String, password: String) {
        _isLoading.value = true
        val client = APIConfig.build().userRegister(email, password)
        client.enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    val baseResponse = response.body()!!
                    if (baseResponse.message.equals("Berhasil mendaftar akun")) {
                        _userResponse.value = baseResponse
                        _message.value = Event(baseResponse.message)
                    } else {
                        _message.value = Event(baseResponse.message)
                    }
                } else {
                    _message.value = Event(response.message().toString())
                    Log.e(
                        TAG,
                        "Failure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })

    }

}