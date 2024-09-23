package com.example.desafio2_nivel1_teste1

data class Student(
    val name: String,
    val className: String,
    val notes: MutableList<Double> = mutableListOf()
) {
    fun calculateAverage(): Double {
        return if (notes.isNotEmpty()) notes.average() else 0.0
    }

    fun isApproved(): Boolean {
        return calculateAverage() >= 7.0
    }
}
