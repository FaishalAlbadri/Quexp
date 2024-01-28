package com.bintang.quexp.ui.fragment.awards

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.data.awards.AwardsItem
import com.bintang.quexp.data.awards.AwardsResponse
import com.bintang.quexp.util.UserPreferences
import com.bintang.quexp.util.viewmodel.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AwardsViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    companion object {
        private const val TAG = "AwardsViewModel"
    }

    private val _awardsResponse = MutableLiveData<List<AwardsItem>>()
    val awardsResponse: LiveData<List<AwardsItem>> = _awardsResponse

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun awards() {
        viewModelScope.launch {
            val client = APIConfig.build(getTokenUser()).awards(getIdUser())
            client.enqueue(object : Callback<AwardsResponse> {
                override fun onResponse(
                    call: Call<AwardsResponse>,
                    response: Response<AwardsResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val awardsResponse = response.body()
                        if (awardsResponse!!.message.equals("Berhasil")) {
                            _awardsResponse.value = awardsResponse.awards
                        } else {
                            _message.value = Event(awardsResponse.message)
                        }
                    } else {
                        _message.value = Event(response.message().toString())
                        Log.e(
                            TAG,
                            "Failure: ${response.message()}, ${response.body()?.message.toString()}"
                        )
                    }
                }

                override fun onFailure(call: Call<AwardsResponse>, t: Throwable) {
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