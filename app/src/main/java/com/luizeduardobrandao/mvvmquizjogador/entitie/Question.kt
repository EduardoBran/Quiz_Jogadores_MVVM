package com.luizeduardobrandao.mvvmquizjogador.entitie

data class Question(
    val id: Int,
    val question: String,
    val clubs: List<String>,
    val options: List<String>,
    val answer: String,
    val level: String,
    val dica: String
)
