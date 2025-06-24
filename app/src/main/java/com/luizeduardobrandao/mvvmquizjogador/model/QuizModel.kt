package com.luizeduardobrandao.mvvmquizjogador.model

import com.luizeduardobrandao.mvvmquizjogador.entitie.Question
import com.luizeduardobrandao.mvvmquizjogador.repository.QuizRepository

/**
 * Lógica de domínio do Quiz:
 * – Carrega lista de perguntas
 * – Mantém índice
 * – Avalia respostas e decide reinício / avanço / fim
 */
class QuizModel(private val repo: QuizRepository) {

    private lateinit var questions: List<Question>
    private var index = 0

    fun load(level: Int) {
        questions = repo.loadQuestionByLevel(level)
        index = 0
    }

    fun current(): Question = questions[index]
    fun currentIndex(): Int = index + 1
    fun total(): Int = questions.size

    /**
     * Avalia a opção:
     * – retorna true se acertou e ainda há próxima
     * – retorna null se acertou mas era última (deve ir para resultado)
     * – retorna false se errou (reinicia índice a zero)
     *
     * NÃO avança o índice aqui — apenas informa o resultado.
     */
    fun checkAnswer(option: String): Boolean? {
        val correct = option == current().answer
        if (!correct) {
            index = 0
            return false
        }
        return if (index == questions.lastIndex) {
            null
        } else {
            true
        }
    }

    /** Chama após delay para avançar ou reiniciar de fato */
    fun moveNext(result: Boolean?) {
        when (result) {
            true  -> index++        // avança
            false -> index = 0      // reinicia
            null  -> { /* fim, não importa */ }
        }
    }
}