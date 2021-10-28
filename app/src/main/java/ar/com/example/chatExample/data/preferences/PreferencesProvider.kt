package ar.com.example.chatExample.data.preferences

import android.content.SharedPreferences
import ar.com.example.chatExample.application.AppConstants
import javax.inject.Inject

class PreferencesProvider @Inject constructor(private val preferences:SharedPreferences) {

    fun saveMyToken(token:String?){
        preferences.edit().putString(
            AppConstants.KEY_MODE,
            token
        ).apply()
    }

    fun getMyToken():String? = preferences.getString(AppConstants.KEY_MODE, null)
}