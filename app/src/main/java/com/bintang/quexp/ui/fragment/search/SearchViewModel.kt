package com.bintang.quexp.ui.fragment.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.data.places.PlacesItem
import com.bintang.quexp.data.places.PlacesResponse
import com.bintang.quexp.util.UserPreferences
import com.bintang.quexp.util.viewmodel.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(private val userPreferences: UserPreferences) : ViewModel()  {

    companion object {
        private const val TAG = "SearchViewModel"
    }

    private val _searchResponse = MutableLiveData<List<PlacesItem>>()
    val searchResponse: LiveData<List<PlacesItem>> = _searchResponse

    private val _placesResponse = MutableLiveData<PlacesResponse>()
    val placesResponse: LiveData<PlacesResponse> = _placesResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun search(key: String) {
        viewModelScope.launch {
            val client = APIConfig.build(getTokenUser()).placesSearch(key)
            client.enqueue(object : Callback<PlacesResponse> {
                override fun onResponse(
                    call: Call<PlacesResponse>,
                    response: Response<PlacesResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val searchResponse = response.body()
                        if (searchResponse!!.message.equals("Berhasil")) {
                            _searchResponse.value = searchResponse.places
                        } else {
                            _message.value = Event(searchResponse.message)
                        }
                    } else {
                        _message.value = Event(response.message().toString())
                        Log.e(
                            TAG,
                            "Failure: ${response.message()}, ${response.body()?.message.toString()}"
                        )
                    }
                }

                override fun onFailure(call: Call<PlacesResponse>, t: Throwable) {
                    _message.value = Event(t.message.toString())
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })

        }
    }

    fun places() {
        viewModelScope.launch {
            _isLoading.value = true
            val client = APIConfig.build(getTokenUser()).places("home")
            client.enqueue(object : Callback<PlacesResponse> {
                override fun onResponse(
                    call: Call<PlacesResponse>,
                    response: Response<PlacesResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val placesResponse = response.body()
                        if (placesResponse!!.message.equals("Berhasil")) {
                            _placesResponse.value = placesResponse!!
                        } else {
                            _message.value = Event(placesResponse.message)
                        }
                    } else {
                        _message.value = Event(response.message().toString())
                        Log.e(
                            TAG,
                            "Failure: ${response.message()}, ${response.body()?.message.toString()}"
                        )
                    }
                }

                override fun onFailure(call: Call<PlacesResponse>, t: Throwable) {
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
}