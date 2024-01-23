package com.bintang.quexp.ui.roadmap

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.data.roadmap.RoadmapItem
import com.bintang.quexp.data.roadmap.RoadmapResponse
import com.bintang.quexp.util.UserPreferences
import com.bintang.quexp.util.viewmodel.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoadmapViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    companion object {
        private const val TAG = "RoadmapViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _roadmapResponse = MutableLiveData<List<RoadmapItem>>()
    val roadmapResponse: LiveData<List<RoadmapItem>> = _roadmapResponse

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun roadmap() {
        viewModelScope.launch {
            _isLoading.value = true
            val client = APIConfig.build(getTokenUser()).roadmap(getIdUser())
            client.enqueue(object : Callback<RoadmapResponse> {
                override fun onResponse(
                    call: Call<RoadmapResponse>,
                    response: Response<RoadmapResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val roadmapResponse = response.body()
                        if (roadmapResponse!!.message.equals("Berhasil")) {
                            _roadmapResponse.value = roadmapResponse.roadmap
                        } else {
                            _message.value = Event(roadmapResponse.message)
                        }
                    } else {
                        _message.value = Event(response.message().toString())
                        Log.e(
                            TAG,
                            "Failure: ${response.message()}, ${response.body()?.message.toString()}"
                        )
                    }
                }

                override fun onFailure(call: Call<RoadmapResponse>, t: Throwable) {
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