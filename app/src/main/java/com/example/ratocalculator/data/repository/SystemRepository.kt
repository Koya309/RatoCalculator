package com.example.ratocalculator.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ratocalculator.settings.model.AppLanguage
import com.example.ratocalculator.settings.model.AppTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


private val Context.systemDataStore by preferencesDataStore(name = "system_setting_pref")

@Singleton
class SystemRepository @Inject constructor(
    @ApplicationContext private val context: Context
){
    private val APP_THEME_KEY = stringPreferencesKey("app_theme")
    private val APP_LANGUAGE_KEY = stringPreferencesKey("app_language")

    val appTheme: Flow<AppTheme> = context.systemDataStore.data
        .map { preferences ->
            val saveTheme = preferences[APP_THEME_KEY] ?: AppTheme.SYSTEM.name
            try {
                AppTheme.valueOf(saveTheme)
            } catch (_: IllegalArgumentException){
                AppTheme.SYSTEM
            }
        }

    suspend fun saveAppTheme(theme: AppTheme){
        context.systemDataStore.edit { preferences ->
            preferences[APP_THEME_KEY] =theme.name
        }
    }

    val appLanguage: Flow<AppLanguage> = context.systemDataStore.data
        .map { preferences ->
            val saved = preferences[APP_LANGUAGE_KEY] ?: AppLanguage.SYSTEM.name
            try {
                AppLanguage.valueOf(saved)
            } catch (_: Exception){
                AppLanguage.SYSTEM
            }
        }

    suspend fun saveAppLanguage(language: AppLanguage){
        context.systemDataStore.edit { preferences ->
            preferences[APP_LANGUAGE_KEY] = language.name
        }
    }
}