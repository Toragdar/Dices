package com.mindflip.dices.domain

import com.mindflip.dices.domain.model.CombinationType
import com.mindflip.dices.domain.model.Dice

object AiLogic {
    fun chooseDiceToReroll(
        aiDices: Array<Dice>,
        playerCombination: CombinationType
    ): List<Int> {
        val aiValues = aiDices.map { it.diceFaces[it.currentFaceIndex].faceValue }
        val freq = aiValues.groupingBy { it }.eachCount()
        val counts = freq.entries.sortedByDescending { it.value }

        val aiCombination = CombinationChecker.check(aiDices)

        // если у ИИ уже Poker — не трогаем ничего
        if (aiCombination == CombinationType.POKER) return emptyList()

        return when (aiCombination) {
            CombinationType.FOUR_OF_A_KIND -> emptyList() // хорошая комбинация — не трогаем
            CombinationType.FULL_HOUSE -> emptyList() // тоже норм
            CombinationType.SET -> { // пробуем поймать фулл-хаус
                val setValue = counts.first().key
                aiValues.mapIndexedNotNull { index, v -> if (v != setValue) index else null }
            }
            CombinationType.TWO_PAIRS -> { // пытаемся поймать фулл-хаус
                val pairValues = counts.take(2).map { it.key }
                aiValues.mapIndexedNotNull { index, v -> if (v !in pairValues) index else null }
            }
            CombinationType.PAIR -> { // оставляем пару, перебрасываем остальное
                val pairValue = counts.first().key
                aiValues.mapIndexedNotNull { index, v -> if (v != pairValue) index else null }
            }
            CombinationType.NONE -> { // ничего нет — оставляем старшую, перебрасываем остальные 4
                val maxValue = aiValues.maxOrNull() ?: return aiValues.indices.toList()
                aiValues.mapIndexedNotNull { index, v -> if (v != maxValue) index else null }
            }
            CombinationType.SMALL_STRAIGHT, CombinationType.BIG_STRAIGHT -> emptyList() // стрит — не трогаем
            else -> { // на всякий случай для непредвиденных случаев
                emptyList()
            }
        }
    }
}