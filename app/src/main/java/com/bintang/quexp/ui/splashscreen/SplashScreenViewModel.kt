package com.bintang.quexp.ui.splashscreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.data.local.UserData
import com.bintang.quexp.data.user.UserItem
import com.bintang.quexp.data.user.UserResponse
import com.bintang.quexp.util.UserPreferences
import com.bintang.quexp.util.viewmodel.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashScreenViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    companion object {
        private const val TAG = "SplashScreenViewModel"
    }

    private val _userResponse = MutableLiveData<UserItem>()
    val userResponse: LiveData<UserItem> = _userResponse

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun profile() {
        viewModelScope.launch {
            val client = APIConfig.build(getTokenUser()).userProfile(getIdUser())
            client.enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.body()!!.message.equals("Berhasil")) {
                        _userResponse.value = response.body()!!.userItem
                    } else {
                        _message.value = Event(response.body()?.message.toString())
                        Log.e(
                            TAG,
                            "Failure: ${response.message()}, ${response.body()?.message.toString()}"
                        )
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    _message.value = Event(t.message.toString())
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        }
    }

    /**
     * -----------------------------------  UserPreferences  -----------------------------------
     */

    fun saveSession(userData: UserData) {
        viewModelScope.launch {
            userPreferences.saveSession(userData)
        }
    }

    suspend fun getIdUser(): String {
        return userPreferences.getSession().first().id_user
    }

    suspend fun getTokenUser(): String {
        return userPreferences.getSession().first().user_token
    }

    fun getSessionUser(): LiveData<UserData> {
        return userPreferences.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.logout()
        }
    }
}