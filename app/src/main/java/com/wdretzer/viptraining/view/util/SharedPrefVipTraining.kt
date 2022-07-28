package com.wdretzer.viptraining.view.util

import android.content.Context
import android.content.SharedPreferences


// Classe para salvar dados do Perfil do usuário no Shared Preferences:
class SharedPrefVipTraining {
    private val sharedPref: SharedPreferences = AppUtil
        .appContext?.getSharedPreferences("viptraining", Context.MODE_PRIVATE)
        ?: throw IllegalArgumentException("shared preferences error!")

    // Método para salvar dados
    fun saveString(id: String, string: String) {
        sharedPref.edit()?.putString(id, string)?.apply()
    }

    // Método para ler dados salvos
    fun readString(id: String): String {
        return sharedPref.getString(id, "") ?: ""
    }

    companion object {
        val instance: SharedPrefVipTraining by lazy { SharedPrefVipTraining() }
    }
}
