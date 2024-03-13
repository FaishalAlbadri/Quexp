package com.bintang.quexp.ui.fragment.ranking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.data.ranking.RankingItem
import com.bintang.quexp.data.ranking.RankingResponse
import com.bintang.quexp.util.UserPreferences
import com.bintang.quexp.util.viewmodel.Event
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RankingViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    companion object {
        private const val TAG = "RankingViewModel"
    }

    private val _rankingResponse = MutableLiveData<List<RankingItem>>()
    val rankingResponse: LiveData<List<RankingItem>> = _rankingResponse

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    fun ranking() {
        viewModelScope.launch {
            val client = APIConfig.build(getTokenUser()).ranking()
            client.enqueue(object : Callback<RankingResponse> {
                override fun onResponse(
                    call: Call<RankingResponse>,
                    response: Response<RankingResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val rankingResponse = response.body()
                        if (rankingResponse!!.message.equals("Berhasil")) {
                            _rankingResponse.value = rankingResponse.ranking
                        } else {
                            _message.value = Event(rankingResponse.message)
                        }
                    } else {
                        _message.value = Event(response.message().toString())
                        Log.e(
                            TAG,
                            "Failure: ${response.message()}, ${response.body()?.message.toString()}"
                        )
                    }
                }

                override fun onFailure(call: Call<RankingResponse>, t: Throwable) {
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