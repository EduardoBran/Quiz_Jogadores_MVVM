package com.luizeduardobrandao.mvvmquizjogador.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application): AndroidViewModel(application) {

    // Evento de navegação para LevelsActivity
    private val _navigateToLevels = MutableLiveData<Boolean>()
    val navigateToLevels: LiveData<Boolean> = _navigateToLevels

    // Chamar ao clicar no botão “Iniciar”
    fun onStartQuizClicked(){
        _navigateToLevels.value = true
    }

    // Após navegar, resetar o evento
    fun onNavigationDone(){
        _navigateToLevels.value = false
    }
}