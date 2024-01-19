package com.bintang.quexp.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bintang.quexp.data.local.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val ID_USER_KEY = stringPreferencesKey("id_user")
    private val USER_NAME_KEY = stringPreferencesKey("user_name")
    private val USER_TOKEN_KEY = stringPreferencesKey("user_token")
    private val STATE_KEY = booleanPreferencesKey("state")

    fun getSession(): Flow<UserData> {
        return dataStore.data.map { preferences ->
            UserData(
                preferences[ID_USER_KEY] ?: "",
                preferences[USER_NAME_KEY] ?: "",
                preferences[USER_TOKEN_KEY] ?: "",
                preferences[STATE_KEY] ?: false
            )
        }
    }

    suspend fun saveSession(user: UserData) {
        dataStore.edit { preferences ->
            preferences[ID_USER_KEY] = user.id_user
            preferences[USER_NAME_KEY] = user.user_name
            preferences[USER_TOKEN_KEY] = user.user_token
            preferences[STATE_KEY] = user.isLogin
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null
        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}