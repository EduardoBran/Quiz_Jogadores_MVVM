package com.luizeduardobrandao.mvvmquizjogador.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.luizeduardobrandao.mvvmquizjogador.R
import com.luizeduardobrandao.mvvmquizjogador.entitie.Question

// Carrega as perguntas do quiz a partir de um JSON em res/raw/quiz_data.json
class QuizRepository(private val context: Context) {

    // Lê todas as perguntas e retorna até 10 perguntas do nível solicitado
    // @param level número do nível a filtrar (ex.: 1, 2, ...)
    fun loadQuestionByLevel(level: Int): List<Question> {

        // 1. Lê o conteúdo do arquivo json em raw
        val rawJson = context.resources
            .openRawResource(R.raw.quiz_data)
            .bufferedReader()
            .use { it.readText() }

        // 2. Define o tipo genérico para deserialização
        val listType = object: TypeToken<List<Question>>() {}.type

        // 3. Converte JSON para lista de Question
        val allQuestions: List<Question> = Gson().fromJson(rawJson, listType)

        // 4. Filtra e retorna apenas as perguntas do nível desejado e limita a 10 itens
        return allQuestions
            .filter { it.level.toIntOrNull() == level }
            .take(10)
    }
}