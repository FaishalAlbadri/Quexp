package com.bintang.quexp.util.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bintang.quexp.ui.fragment.awards.AwardsViewModel
import com.bintang.quexp.ui.fragment.home.HomeViewModel
import com.bintang.quexp.ui.fragment.profile.ProfileViewModel
import com.bintang.quexp.ui.fragment.scan.QrCodeViewModel
import com.bintang.quexp.ui.fragment.visited.VisitedViewModel
import com.bintang.quexp.ui.login.LoginViewModel
import com.bintang.quexp.ui.roadmap.RoadmapViewModel
import com.bintang.quexp.ui.splashscreen.SplashScreenViewModel
import com.bintang.quexp.util.UserPreferences

class ViewModelFactory(private val userPreferences: UserPreferences) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SplashScreenViewModel::class.java) -> {
                SplashScreenViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(QrCodeViewModel::class.java) -> {
                QrCodeViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(RoadmapViewModel::class.java) -> {
                RoadmapViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(AwardsViewModel::class.java) -> {
                AwardsViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(VisitedViewModel::class.java) -> {
                VisitedViewModel(userPreferences) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}