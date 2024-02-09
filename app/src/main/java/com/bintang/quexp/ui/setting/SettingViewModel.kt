package com.bintang.quexp.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintang.quexp.util.UserPreferences
import kotlinx.coroutines.launch

class SettingViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    companion object {
        private const val TAG = "SettingViewModel"
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.logout()
        }
    }
}