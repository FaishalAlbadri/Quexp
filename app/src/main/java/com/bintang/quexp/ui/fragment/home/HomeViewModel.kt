package com.bintang.quexp.ui.fragment.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.data.awards.AwardsItem
import com.bintang.quexp.data.awards.AwardsResponse
import com.bintang.quexp.data.local.UserData
import com.bintang.quexp.data.news.NewsItem
import com.bintang.quexp.data.news.NewsResponse
import com.bintang.quexp.util.UserPreferences
import com.bintang.quexp.util.viewmodel.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _newsResponse = MutableLiveData<List<NewsItem>>()
    val newsResponse: LiveData<List<NewsItem>> = _newsResponse

    private val _awardsResponse = MutableLiveData<AwardsItem>()
    val awardsResponse: LiveData<AwardsItem> = _awardsResponse

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun news() {
        viewModelScope.launch {
            _isLoading.value = true
            val client = APIConfig.build(getTokenUser()).news()
            client.enqueue(object : Callback<NewsResponse> {
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val newsResponse = response.body()
                        if (newsResponse!!.message.equals("Berhasil")) {
                            _newsResponse.value = newsResponse.news
                        } else {
                            _message.value = Event(newsResponse.message)
                        }
                    } else {
                        _message.value = Event(response.message().toString())
                        Log.e(
                            TAG,
                            "Failure: ${response.message()}, ${response.body()?.message.toString()}"
                        )
                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    _isLoading.value = false
                    _message.value = Event(t.message.toString())
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })

        }
    }

    fun awards(){
        viewModelScope.launch {
            _isLoading.value = true
            val client = APIConfig.build(getTokenUser()).awards(getIdUser())
            client.enqueue(object : Callback<AwardsResponse> {
                override fun onResponse(
                    call: Call<AwardsResponse>,
                    response: Response<AwardsResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val awardsResponse = response.body()
                        if (awardsResponse!!.message.equals("Berhasil")) {
                            val countData = awardsResponse.awards.size
                            _awardsResponse.value = awardsResponse.awards.get(countData - 1)
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

    fun getSessionUser(): LiveData<UserData> {
        return userPreferences.getSession().asLiveData()
    }

}