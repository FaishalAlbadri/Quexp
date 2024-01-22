package com.bintang.quexp.ui.fragment.scan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.data.BaseResponse
import com.bintang.quexp.util.UserPreferences
import com.bintang.quexp.util.viewmodel.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QrCodeViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    companion object {
        private const val TAG = "QrCodeViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun scanQrCode(placeId: String, userLat: Double, userLong: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            val client = APIConfig.build(getTokenUser()).visitedAdd(placeId, getIdUser(), userLat, userLong)
            client.enqueue(object : Callback<BaseResponse> {
                override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val userResponse = response.body()
                        _message.value = Event(userResponse!!.message)
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

    suspend fun getTokenUser(): String {
        return userPreferences.getSession().first().user_token
    }

    suspend fun getIdUser(): String {
        return userPreferences.getSession().first().id_user
    }
}