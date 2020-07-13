package stta.gabriel.ta_gabriel.util

import android.content.Context
import androidx.preference.PreferenceManager

class SharedPrefs(context: Context) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor = preferences.edit()

    fun saveString(key: String, value: String) {
        editor.apply {
            putString(key, value)
            apply()
        }
    }

    fun saveDouble(key: String, value: Double) {
        editor.apply {
            putLong(key, java.lang.Double.doubleToRawLongBits(value))
            apply()
        }
    }

    fun saveInt(key: String, value: Int) {
        editor.apply {
            putInt(key, value)
            apply()
        }
    }

    fun saveBoolean(key: String, value: Boolean) {
        editor.apply {
            putBoolean(key, value)
            apply()
        }
    }

    fun saveFloat(key: String, value: Float) {
        editor.apply {
            putFloat(key, value)
            apply()
        }
    }

    fun getInteger(key: String): Int {
        return preferences.getInt(key, 0)
    }

    fun getDouble(key: String): Double {
        val doub = java.lang.Double.longBitsToDouble(
            preferences.getLong(
                key
                , java.lang.Double.doubleToRawLongBits(0.0)
            )
        )
        return doub
    }

    fun getFloat(key: String): Float {
        return preferences.getFloat(key, 0F)
    }

    fun getString(key: String): String {
        return preferences.getString(key, "").toString()
    }

    fun getBoolean(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }
}


const val IS_LOGGED_IN = "IS_LOGGED_IN"
const val USER_VALUE = "USER_VALUE"