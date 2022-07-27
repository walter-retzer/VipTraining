package com.wdretzer.viptraining.util

import android.content.Context
import android.content.SharedPreferences


class SharedPrefVipTraining {
    private val sharedPref: SharedPreferences = AppUtil
        .appContext?.getSharedPreferences("viptraining", Context.MODE_PRIVATE)
        ?: throw IllegalArgumentException("shared preferences error!")

    fun saveString(id: String, string: String) {
        sharedPref.edit()?.putString(id, string)?.apply()
    }

    fun readString(id: String): String {
        return sharedPref.getString(id, "") ?: ""
    }

    companion object {
        val instance: SharedPrefVipTraining by lazy { SharedPrefVipTraining() }
    }
}
