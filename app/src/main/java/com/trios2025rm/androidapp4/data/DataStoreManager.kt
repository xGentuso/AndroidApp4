package com.trios2025rm.androidapp4.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) {
    private val counterKey = intPreferencesKey("counter_value")
    private val minValueKey = intPreferencesKey("min_value")
    private val maxValueKey = intPreferencesKey("max_value")

    val counterValue: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[counterKey] ?: 0
        }

    val minValue: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[minValueKey] ?: -100
        }

    val maxValue: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[maxValueKey] ?: 100
        }

    suspend fun saveCounterValue(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[counterKey] = value
        }
    }

    suspend fun saveMinValue(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[minValueKey] = value
        }
    }

    suspend fun saveMaxValue(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[maxValueKey] = value
        }
    }
} 