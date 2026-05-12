package com.mindflip.dices.domain.model

enum class GameState {
    START_ROUND,
    PLAYER_CHOOSE,
    AI_CHOOSE,
    ROLL_PHASE,
    END_ROUND,
    GAME_OVER
}