package com.luizeduardobrandao.mvvmquizjogador.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.luizeduardobrandao.mvvmquizjogador.helper.LevelsSharedPreferences

class LevelsViewModel(app: Application): AndroidViewModel(app) {

    // Nível atualmente desbloqueado (inicia em 1)
    private val _unlockedLevel = MutableLiveData<Int>().apply {
        value = LevelsSharedPreferences.getUnlockedLevel(getApplication())
    }
    val unlockedLevel: LiveData<Int> = _unlockedLevel

    // Evento de navegação: quando não nulo, indica qual nível abrir
    private val _navigateToQuiz = MutableLiveData<Int?>()
    val navigateToQuiz: LiveData<Int?> = _navigateToQuiz

    // Chamado ao tocar em um nível ativo
    fun onLevelClicked(level: Int) {
        if (level <= (_unlockedLevel.value ?: 1)) {
            _navigateToQuiz.value = level
        }
    }

    // Após navegar, reseta o evento para não disparar novamente */
    fun onNavigationDone() {
        _navigateToQuiz.value = null
    }

    // Futuro: desbloqueia o próximo nível até o máximo de 10
    fun unlockNextLevel() {
        val next = (_unlockedLevel.value ?: 1) + 1
        val unlocked = next.coerceAtMost(10)
        _unlockedLevel.value = unlocked

        // persiste
        LevelsSharedPreferences.setUnlockedLevel(getApplication(), unlocked)
    }
}