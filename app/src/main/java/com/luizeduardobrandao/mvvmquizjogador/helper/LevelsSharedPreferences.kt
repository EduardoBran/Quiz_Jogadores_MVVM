package com.luizeduardobrandao.mvvmquizjogador.helper

import android.content.Context

object LevelsSharedPreferences {

    private const val PREFS_NAME = "quiz_levels_prefs"
    private const val KEY_UNLOCKED = "unlocked_leves"

    // Retorna o nível atualmente desbloqueado (padrão = 1)
    fun getUnlockedLevel(context: Context): Int =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getInt(KEY_UNLOCKED, 1)

    // Salva o nível desbloqueado
    fun setUnlockedLevel(context: Context, level: Int){
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_UNLOCKED, level)
            .apply()
    }
}