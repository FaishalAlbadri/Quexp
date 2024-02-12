package com.bintang.quexp.ui.setting.change.profile

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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangeProfileViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    companion object {
        private const val TAG = "ChangeProfileViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userResponse = MutableLiveData<BaseResponse>()
    val userResponse: LiveData<BaseResponse> = _userResponse

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun changeProfile(map: MutableMap<String, RequestBody>) {
        viewModelScope.launch {
            _isLoading.value = true
            map.put(
                "id_user", getIdUser().toRequestBody("text/plain".toMediaType())
            )
            val client = APIConfig.build(getTokenUser())
                .userEditProfile(map)
            client.enqueue(object : Callback<BaseResponse> {
                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val baseResponse = response.body()!!
                        if (baseResponse.message.equals("Berhasil update")) {
                            _userResponse.value = baseResponse
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

    suspend fun getTokenUser(): String {
        return userPreferences.getSession().first().user_token
    }

    suspend fun getIdUser(): String {
        return userPreferences.getSession().first().id_user
    }
}