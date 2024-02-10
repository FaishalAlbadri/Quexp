package com.bintang.quexp.ui.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintang.quexp.util.UserPreferences
import kotlinx.coroutines.launch
import java.io.File


class SettingViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    companion object {
        private const val TAG = "SettingViewModel"
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.logout()
        }
    }

    fun deleteCache(context: Context) {
        try {
            val dir = context.cacheDir
            deleteDir(dir)
        } catch (e: Exception) {
        }
    }

    fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }
}