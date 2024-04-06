package com.bintang.quexp.ui.fragment.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.data.app.HomeResponse
import com.bintang.quexp.data.banner.BannerItem
import com.bintang.quexp.data.category.CategoryItem
import com.bintang.quexp.data.local.UserData
import com.bintang.quexp.data.news.NewsItem
import com.bintang.quexp.data.places.PlacesItem
import com.bintang.quexp.data.ranking.RankingItem
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

    private val _placesResponse = MutableLiveData<List<PlacesItem>>()
    val placesResponse: LiveData<List<PlacesItem>> = _placesResponse

    private val _rankingResponse = MutableLiveData<List<RankingItem>>()
    val rankingResponse: LiveData<List<RankingItem>> = _rankingResponse

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun home() {
        viewModelScope.launch {
            _isLoading.value = true
            val client = APIConfig.build(getTokenUser()).appHome(getIdUser())
            client.enqueue(object : Callback<HomeResponse> {
                override fun onResponse(
                    call: Call<HomeResponse>,
                    response: Response<HomeResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val homeResponse = response.body()
                        if (homeResponse!!.message.equals("Berhasil")) {
                            _bannerResponse.value = homeResponse.banner
                            _categoryResponse.value = homeResponse.category
                            _newsResponse.value = homeResponse.news
                            _placesResponse.value = homeResponse.placesPopuler
                            _rankingResponse.value = homeResponse.ranking
                        } else {
                            _message.value = Event(homeResponse.message)
                        }
                    } else {
                        _message.value = Event(response.message().toString())
                        Log.e(
                            TAG,
                            "Failure: ${response.message()}, ${response.body()?.message.toString()}"
                        )
                    }
                }

                override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
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