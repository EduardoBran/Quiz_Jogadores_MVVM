package com.luizeduardobrandao.mvvmquizjogador.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LevelsViewModel: ViewModel() {

    // Nível atualmente desbloqueado (inicia em 1)
    private val _unlockedLevel = MutableLiveData(1)
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
        _unlockedLevel.value = next.coerceAtMost(10)
    }
}