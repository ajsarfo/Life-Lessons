package com.sarftec.lifelessons.application.file

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val APP_CREATED = booleanPreferencesKey("app_created")
val App_START_UP_TIMES = intPreferencesKey("app_start_up_times")

val Context.dataStore by preferencesDataStore(name = "settings_store")

fun <T> Context.readSettings(key: Preferences.Key<T>, default: T): Flow<T> {
    return dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw Exception("Setting data store exception occurred")
        }
        .map { preferences ->
            preferences[key] ?: default
        }
}

suspend fun <T> Context.editSettings(key: Preferences.Key<T>, value: T) {
    dataStore.edit { preferences ->
        preferences[key] = value
    }
}