package com.mindflip.dices.domain.model

enum class CombinationType(
    val rank: Int,
    val damageAmount: Int
) {
    POKER(9, 17),
    FOUR_OF_A_KIND(8, 15),
    FULL_HOUSE(7, 13),
    BIG_STRAIGHT(6, 11),
    SMALL_STRAIGHT(5, 9),
    SET(4, 7),
    TWO_PAIRS(3, 5),
    PAIR(2, 3),
    NONE(1, 0)
}