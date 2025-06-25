package com.luizeduardobrandao.mvvmquizjogador.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ResultViewModel(application: Application): AndroidViewModel(application) {

    private val _level = MutableLiveData<Int>()
    val level: LiveData<Int> = _level

    // chama na activiy logo após obter o Intent extra
    fun setLevel(level: Int) {
        _level.value = level
    }
}