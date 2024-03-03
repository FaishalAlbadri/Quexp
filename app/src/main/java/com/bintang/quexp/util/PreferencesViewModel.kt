package com.bintang.quexp.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bintang.quexp.data.local.UserData
import kotlinx.coroutines.launch

class PreferencesViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    fun getSessionUser(): LiveData<UserData> {
        return userPreferences.getSession().asLiveData()
    }

    fun saveFeatureDiscovery() {
        viewModelScope.launch {
            userPreferences.saveFeatureDiscovery(true)
        }
    }

    fun saveARDiscovery() {
        viewModelScope.launch {
            userPreferences.saveARDiscovery(true)
        }
    }
}