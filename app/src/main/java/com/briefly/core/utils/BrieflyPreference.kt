package com.briefly.core.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.briefly.BuildConfig
import kotlinx.coroutines.flow.first

const val preferenceName = BuildConfig.APPLICATION_ID

class BrieflyPreference(private val context: Context){
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = preferenceName)
    private object Keys{
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_ACTIVE = booleanPreferencesKey("user_active")
    }

    suspend fun writeToDataStore(value: String){
        context.datastore.edit {
            it[BrieflyPreference.Keys.USER_NAME] = value

        }
    }

    suspend fun readFromDataStore(): String?{
        val preferences = context.datastore.data.first()
        return preferences[BrieflyPreference.Keys.USER_NAME]
    }

    suspend fun clearDataStore() {
        context.datastore.edit {
            it.clear()
        }
    }
}