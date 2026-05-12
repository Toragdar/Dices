package com.mindflip.dices.viewmodel

import androidx.lifecycle.ViewModel
import com.mindflip.dices.domain.model.AiOpponent
import com.mindflip.dices.domain.model.CombinationType
import com.mindflip.dices.domain.model.EvaluatedCombination
import com.mindflip.dices.domain.model.GameState
import com.mindflip.dices.domain.model.Player
import com.mindflip.dices.domain.model.ResultCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel (){

    private val _player = MutableStateFlow(Player())
    val playerDices = _player.value.dices.asStateFlow()
    val playerCurrentCombination = _player.value.combination
    val playerCurrentHP = _player.value.hp

    private val _ai = MutableStateFlow(AiOpponent())
    val aiDices = _ai.value.dices.asStateFlow()
    val aiCurrentCombination = _ai.value.combination
    val aiCurrentHP = _ai.value.hp

    private val _gameState = MutableStateFlow(GameState.START_ROUND)
    val gameState = _gameState.asStateFlow()

    private val _winner = MutableStateFlow(ResultCases.NONE)
    val winner = _winner.asStateFlow()

    private val _isGameOver = MutableStateFlow(false)
    val isGameOver = _isGameOver.asStateFlow()

    init {
        startInitialRoll()
    }

    private fun startInitialRoll() {
        _player.value.rollAllDices()
        _ai.value.rollAllDices()
        _gameState.value = GameState.PLAYER_CHOOSE
    }

    fun finishTurn(){
        aiMakeDecision(_player.value.combination.value)

        _gameState.value = GameState.ROLL_PHASE

        rollPlayerSelectedDices()
        rollAiSelectedDices()

        _winner.value = getWinner(
            _player.value.evaluate(
                _player.value.dices.value,
                _player.value.combination.value
            ),
            _ai.value.evaluate(
                _ai.value.dices.value,
                _ai.value.combination.value
            )
        )

        applyDamage()

        checkIsGameOver()

        when (isGameOver.value){
            true -> gameOver()
            false -> _gameState.value = GameState.END_ROUND
        }
    }

    fun togglePlayerDiceSelection(index: Int){
        _player.value.selectDice(index)
    }

    fun rollPlayerSelectedDices() {
        _player.value.rollSelectedDices()
    }

    fun rollAiSelectedDices(){
        _ai.value.rollSelectedDices()
    }

    fun aiMakeDecision(playerCombination: CombinationType) {
        _gameState.value = GameState.AI_CHOOSE
        _ai.value.selectDiceForReroll(playerCombination)
    }

    fun getWinner(
        player: EvaluatedCombination,
        opponent: EvaluatedCombination
    ): ResultCases {
        if (player.type.rank > opponent.type.rank) return ResultCases.PLAYER
        if (player.type.rank < opponent.type.rank) return ResultCases.OPPONENT

        // при равенстве типа — смотрим старшие значения
        for (i in player.highValues.indices) {
            val p = player.highValues[i]
            val o = opponent.highValues.getOrNull(i) ?: 0
            if (p > o) return ResultCases.PLAYER
            if (p < o) return ResultCases.OPPONENT
        }

        return ResultCases.DRAW
    }

    fun applyDamage(){
        when (winner.value){
            ResultCases.PLAYER -> {
                _ai.value.takeDamage(
                    (_player.value.combination.value.damageAmount - _ai.value.combination.value.damageAmount).coerceAtLeast(0)
                )
            }
            ResultCases.OPPONENT -> {
                _player.value.takeDamage(
                    (_ai.value.combination.value.damageAmount - _player.value.combination.value.damageAmount).coerceAtLeast(0)
                )
            }
            else -> {

            }
        }
    }

    fun checkIsGameOver(){

    }

    fun startNextRound(){
        _gameState.value = GameState.START_ROUND
        startInitialRoll()
    }

    fun gameOver(){
        _gameState.value = GameState.GAME_OVER
    }
}