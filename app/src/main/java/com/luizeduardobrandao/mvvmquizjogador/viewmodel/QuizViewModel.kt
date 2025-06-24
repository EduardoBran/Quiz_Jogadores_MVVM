package com.luizeduardobrandao.mvvmquizjogador.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.luizeduardobrandao.mvvmquizjogador.entitie.Question
import com.luizeduardobrandao.mvvmquizjogador.repository.QuizRepository

class QuizViewModel(application: Application): AndroidViewModel(application) {

    private val repo = QuizRepository(application)

    // lista completa de questões
    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> = _questions

    // questão atual
    private val _currentQuestion = MutableLiveData<Question>()
    val currentQuestion: LiveData<Question> = _currentQuestion


    // Carrega todas as perguntas do nível e define a primeira
    fun loadQuestions(level: Int) {
        val list = repo.loadQuestionByLevel(level)
        _questions.value = list
        if (list.isNotEmpty()) {
            _currentQuestion.value = list [0]
        }
    }
}