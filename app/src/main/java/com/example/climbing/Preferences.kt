package com.example.climbing

import android.content.Context

object Preferences {
    const val PREF_FCM_TOKEN = "pref_fcm_token"
    const val Id_Percurso = "Id_Percurso"
}

var Context.PREF_FCM_TOKEN : String?
    get(){
        val sharedPref = getSharedPreferences(Preferences.PREF_FCM_TOKEN, Context.MODE_PRIVATE) ?:  return null
        return sharedPref.getString(Preferences.PREF_FCM_TOKEN, "")
    }
    set(value){
        val sharedPref = getSharedPreferences(Preferences.PREF_FCM_TOKEN, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            if (value != null) {
                putString(Preferences.PREF_FCM_TOKEN, value.toString())
            }
            commit()
        }
    }

var Context.Id_Percurso : String?
    get(){
        val sharedPref = getSharedPreferences(Preferences.Id_Percurso, Context.MODE_PRIVATE) ?:  return null
        return sharedPref.getString(Preferences.Id_Percurso, "")
    }
    set(value){
        val sharedPref = getSharedPreferences(Preferences.Id_Percurso, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            if (value != null) {
                putString(Preferences.Id_Percurso, value.toString())
            }
            commit()
        }
    }
