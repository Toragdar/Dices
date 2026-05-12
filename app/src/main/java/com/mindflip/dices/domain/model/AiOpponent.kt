package com.mindflip.dices.domain.model

import com.mindflip.dices.domain.AiLogic
import com.mindflip.dices.domain.CombinationChecker
import com.mindflip.dices.domain.CombinationChecker.evaluateCombination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AiOpponent(
    val dices: MutableStateFlow<Array<Dice>> = MutableStateFlow(
        arrayOf(
            Dice.create(Dice.DiceType.D6),
            Dice.create(Dice.DiceType.D6),
            Dice.create(Dice.DiceType.D6),
            Dice.create(Dice.DiceType.D6),
            Dice.create(Dice.DiceType.D6)
        )
    )
) {
    private val _combination = MutableStateFlow(CombinationType.NONE)
    val combination = _combination.asStateFlow()

    private val maxHp = 30
    private val _hp = MutableStateFlow(maxHp)
    val hp = _hp.asStateFlow()

    private fun updateCombination() {
        _combination.value = CombinationChecker.check(dices.value)
    }

    fun chooseDiceToReroll(playerCombination: CombinationType): List<Int> {
        return AiLogic.chooseDiceToReroll(dices.value, playerCombination)
    }

    fun rollSelectedDices() {
        dices.value.forEach { dice ->
            if (dice.isSelected){
                dice.roll()
                dice.isSelected = !dice.isSelected
            }
        }
        dices.value = dices.value // обновляем, чтобы StateFlow уведомил UI
        updateCombination()
    }

    fun rollAllDices() {
        dices.value.forEach { dice ->
            dice.roll()
        }

        dices.value = dices.value // обновляем, чтобы StateFlow уведомил UI
        updateCombination()
    }

    fun selectDiceForReroll(playerCombination: CombinationType) {
        val indicesToReroll = chooseDiceToReroll(playerCombination)

        dices.value.forEachIndexed { index, dice ->
            dice.isSelected = index in indicesToReroll
        }

        // Принудительно даём знать UI о смене статусов
        dices.value = dices.value
    }

    fun evaluate(dices: Array<Dice>, combinationType: CombinationType): EvaluatedCombination {
        return evaluateCombination(dices, combinationType)
    }

    fun takeDamage(amount: Int){
        _hp.value = (_hp.value - amount).coerceAtLeast(0)
    }
}
