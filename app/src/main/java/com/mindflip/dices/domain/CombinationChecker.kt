package com.mindflip.dices.domain

import com.mindflip.dices.domain.model.CombinationType
import com.mindflip.dices.domain.model.Dice
import com.mindflip.dices.domain.model.EvaluatedCombination
import com.mindflip.dices.domain.model.Player

object CombinationChecker {

    fun check(dices: Array<Dice>): CombinationType {
        val values = dices.map { it.diceFaces[it.currentFaceIndex].faceValue }
        val freq = values.groupingBy { it }.eachCount()
        val countsDesc = freq.values.sortedDescending()

        return when {
            countsDesc.firstOrNull() == 5 -> CombinationType.POKER
            countsDesc.firstOrNull() == 4 -> CombinationType.FOUR_OF_A_KIND
            freq.values.contains(3) && freq.values.contains(2) -> CombinationType.FULL_HOUSE
            isStraight(values) == CombinationType.BIG_STRAIGHT -> CombinationType.BIG_STRAIGHT
            isStraight(values) == CombinationType.SMALL_STRAIGHT -> CombinationType.SMALL_STRAIGHT
            countsDesc.firstOrNull() == 3 -> CombinationType.SET
            freq.values.count { it == 2 } == 2 -> CombinationType.TWO_PAIRS
            freq.values.count { it == 2 } == 1 -> CombinationType.PAIR
            else -> CombinationType.NONE
        }
    }

    private fun isStraight(values: List<Int>): CombinationType? {
        val distinct = values.toSet()
        if (distinct.size != 5) return null
        val sorted = distinct.sorted()
        return when (sorted) {
            listOf(1, 2, 3, 4, 5) -> CombinationType.SMALL_STRAIGHT
            listOf(2, 3, 4, 5, 6) -> CombinationType.BIG_STRAIGHT
            else -> null
        }
    }

    fun evaluateCombination(dices: Array<Dice>, combinationType: CombinationType): EvaluatedCombination {
        val diceValues = dices.map { it.diceFaces[it.currentFaceIndex].faceValue }

        // частотная карта value -> count
        val freq = diceValues.groupingBy { it }.eachCount()

        // сортируем по count desc, затем по value desc
        val highValues = freq.entries
            .sortedWith(compareByDescending<Map.Entry<Int, Int>> { it.value }
                .thenByDescending { it.key })
            .flatMap { entry -> List(entry.value) { entry.key } }

        return EvaluatedCombination(
            type = combinationType,
            highValues = highValues
        )
    }
}