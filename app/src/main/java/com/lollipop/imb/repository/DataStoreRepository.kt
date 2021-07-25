package com.lollipop.e_lapor.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

const val PREFERENCE_NAME = "elapor_preferences"

private object PreferenceKeys {
    val loginStatus = booleanPreferencesKey("loginStatus")
    val noKtp = stringPreferencesKey("noKtp")
    val email = stringPreferencesKey("email")
}

class DataStoreRepository(private val dataStore: DataStore<Preferences>) {

    //Save login state to preferences
    suspend fun saveLoginState(value: Boolean) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.loginStatus] = value
        }
    }

    //Save login data to preferences
    suspend fun saveAuth(noKtp: String, email: String) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.noKtp] = noKtp
            preference[PreferenceKeys.email] = email
        }
    }

    //Read login state from preferences
    val readLoginStatus: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.d(exception.message.toString())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val isPaymentSuccess: Boolean = preferences[PreferenceKeys.loginStatus] ?: false
            isPaymentSuccess
        }

    val readUserData: Flow<List<String>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.d(exception.message.toString())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val data: MutableList<String> = mutableListOf()
            val noKtp: String = preferences[PreferenceKeys.noKtp] ?: "-"
            val email: String = preferences[PreferenceKeys.email] ?: "-"
            data.add(noKtp)
            data.add(email)
            data
        }
}