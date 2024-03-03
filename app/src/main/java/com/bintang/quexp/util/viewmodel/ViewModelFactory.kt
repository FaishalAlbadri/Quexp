package com.bintang.quexp.util.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bintang.quexp.ui.fragment.awards.AwardsViewModel
import com.bintang.quexp.ui.fragment.home.HomeViewModel
import com.bintang.quexp.ui.fragment.profile.ProfileViewModel
import com.bintang.quexp.ui.fragment.scan.QrCodeViewModel
import com.bintang.quexp.ui.fragment.search.SearchViewModel
import com.bintang.quexp.ui.fragment.visited.VisitedViewModel
import com.bintang.quexp.ui.login.LoginViewModel
import com.bintang.quexp.ui.notification.NotificationViewModel
import com.bintang.quexp.ui.roadmap.RoadmapViewModel
import com.bintang.quexp.ui.setting.SettingViewModel
import com.bintang.quexp.ui.setting.change.password.ChangePasswordViewModel
import com.bintang.quexp.ui.setting.change.profile.ChangeProfileViewModel
import com.bintang.quexp.ui.share.ShareViewModel
import com.bintang.quexp.ui.splashscreen.SplashScreenViewModel
import com.bintang.quexp.util.PreferencesViewModel
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
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(NotificationViewModel::class.java) -> {
                NotificationViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(ShareViewModel::class.java) -> {
                ShareViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(ChangePasswordViewModel::class.java) -> {
                ChangePasswordViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(ChangeProfileViewModel::class.java) -> {
                ChangeProfileViewModel(userPreferences) as T
            }
            modelClass.isAssignableFrom(PreferencesViewModel::class.java) -> {
                PreferencesViewModel(userPreferences) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}