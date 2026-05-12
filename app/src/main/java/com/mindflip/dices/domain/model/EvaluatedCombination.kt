package com.mindflip.dices.domain.model

data class EvaluatedCombination(
    val type: CombinationType,
    val highValues: List<Int> // сортируем в порядке приоритета сравнения
)
