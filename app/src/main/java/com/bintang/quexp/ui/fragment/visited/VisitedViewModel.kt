package com.bintang.quexp.ui.fragment.visited

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.data.visited.VisitedItem
import com.bintang.quexp.data.visited.VisitedResponse
import com.bintang.quexp.util.UserPreferences
import com.bintang.quexp.util.viewmodel.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VisitedViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    companion object {
        private const val TAG = "VisitedViewModel"
    }

    private val _visitedResponse = MutableLiveData<List<VisitedItem>>()
    val visitedResponse: LiveData<List<VisitedItem>> = _visitedResponse

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun visited() {
        viewModelScope.launch {
            val client = APIConfig.build(getTokenUser()).visited(getIdUser())
            client.enqueue(object : Callback<VisitedResponse> {
                override fun onResponse(
                    call: Call<VisitedResponse>,
                    response: Response<VisitedResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val visitedResponse = response.body()
                        if (visitedResponse!!.message.equals("Berhasil")) {
                            _visitedResponse.value = visitedResponse.visited
                        } else {
                            _message.value = Event(visitedResponse.message)
                        }
                    } else {
                        _message.value = Event(response.message().toString())
                        Log.e(
                            TAG,
                            "Failure: ${response.message()}, ${response.body()?.message.toString()}"
                        )
                    }
                }

                override fun onFailure(call: Call<VisitedResponse>, t: Throwable) {
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