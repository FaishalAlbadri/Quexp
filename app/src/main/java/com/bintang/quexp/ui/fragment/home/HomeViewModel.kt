package com.bintang.quexp.ui.fragment.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.data.banner.BannerItem
import com.bintang.quexp.data.banner.BannerResponse
import com.bintang.quexp.data.category.CategoryItem
import com.bintang.quexp.data.category.CategoryResponse
import com.bintang.quexp.data.local.UserData
import com.bintang.quexp.data.news.NewsItem
import com.bintang.quexp.data.news.NewsResponse
import com.bintang.quexp.data.places.PlacesResponse
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

    private val _bannerResponse = MutableLiveData<List<BannerItem>>()
    val bannerResponse: LiveData<List<BannerItem>> = _bannerResponse

    private val _categoryResponse = MutableLiveData<List<CategoryItem>>()
    val categoryResponse: LiveData<List<CategoryItem>> = _categoryResponse

    private val _placesResponse = MutableLiveData<PlacesResponse>()
    val placesResponse: LiveData<PlacesResponse> = _placesResponse

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun banner() {
        viewModelScope.launch {
            _isLoading.value = true
            val client = APIConfig.build(getTokenUser()).banner()
            client.enqueue(object : Callback<BannerResponse> {
                override fun onResponse(
                    call: Call<BannerResponse>,
                    response: Response<BannerResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val bannerResponse = response.body()
                        if (bannerResponse!!.message.equals("Berhasil")) {
                            _bannerResponse.value = bannerResponse.banner
                        } else {
                            _isLoading.value = false
                            _message.value = Event(bannerResponse.message)
                        }
                    } else {
                        _isLoading.value = false
                        _message.value = Event(response.message().toString())
                        Log.e(
                            TAG,
                            "Failure: ${response.message()}, ${response.body()?.message.toString()}"
                        )
                    }
                }

                override fun onFailure(call: Call<BannerResponse>, t: Throwable) {
                    _isLoading.value = false
                    _message.value = Event(t.message.toString())
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })

        }
    }

    fun category() {
        viewModelScope.launch {
            _isLoading.value = true
            val client = APIConfig.build(getTokenUser()).category()
            client.enqueue(object : Callback<CategoryResponse> {
                override fun onResponse(
                    call: Call<CategoryResponse>,
                    response: Response<CategoryResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val categoryResponse = response.body()
                        if (categoryResponse!!.message.equals("Berhasil")) {
                            _categoryResponse.value = categoryResponse.category
                        } else {
                            _isLoading.value = false
                            _message.value = Event(categoryResponse.message)
                        }
                    } else {
                        _isLoading.value = false
                        _message.value = Event(response.message().toString())
                        Log.e(
                            TAG,
                            "Failure: ${response.message()}, ${response.body()?.message.toString()}"
                        )
                    }
                }

                override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                    _isLoading.value = false
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
                    if (response.isSuccessful && response.body() != null) {
                        val placesResponse = response.body()
                        if (placesResponse!!.message.equals("Berhasil")) {
                            _placesResponse.value = placesResponse!!
                        } else {
                            _isLoading.value = false
                            _message.value = Event(placesResponse.message)
                        }
                    } else {
                        _isLoading.value = false
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