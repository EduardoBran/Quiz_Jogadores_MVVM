package com.luizeduardobrandao.mvvmquizjogador.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.luizeduardobrandao.mvvmquizjogador.entitie.Question
import com.luizeduardobrandao.mvvmquizjogador.model.QuizModel
import com.luizeduardobrandao.mvvmquizjogador.repository.QuizRepository

class QuizViewModel(app: Application): AndroidViewModel(app) {

    private val model = QuizModel(QuizRepository(app))

    private val _currentQuestion = MutableLiveData<Question>()
    val currentQuestion: LiveData<Question> = _currentQuestion

    private val _questionIndex = MutableLiveData<Int>()
    val questionIndex: LiveData<Int> = _questionIndex

    // resultado da última checagem: true, false ou null
    private val _answerResult = MutableLiveData<Boolean?>()
    val answerResult: LiveData<Boolean?> = _answerResult

    // dispara quando result==null (última pergunta) e deve navegar
    private val _navigateToResult = MutableLiveData<Boolean>()
    val navigateToResult: LiveData<Boolean> = _navigateToResult


    /** Inicia quiz no nível */
    fun loadQuestions(level: Int) {
        model.load(level)
        _currentQuestion.value = model.current()
        _questionIndex.value = model.currentIndex()
    }

    /** Avalia a resposta, mas NÃO avança índice ainda */
    fun submitAnswer(option: String){
        val result = model.checkAnswer(option)
        _answerResult.value = result

        // se resposta era da última pergunta → dispara evento de navegação
        if (result == null) {
            _navigateToResult.value = true
        }
    }

    /**
     * Depois do delay na Activity, chama este mét0do para:
     * – avançar ou reiniciar o índice no Model
     * – atualizar LiveData de pergunta e índice ou disparar navegação
     */
    fun moveToNext() {
        val result = _answerResult.value
        model.moveNext(result)

        when (result) {
            true -> {
                _currentQuestion.value = model.current()
                _questionIndex.value = model.currentIndex()
            }
            false -> {
                _currentQuestion.value = model.current()
                _questionIndex.value = model.currentIndex()
            }
            null -> {
                _navigateToResult.value = true
            }
        }
        _answerResult.value = null
    }

    fun doneNavigation() {
        _navigateToResult.value = false
    }
}