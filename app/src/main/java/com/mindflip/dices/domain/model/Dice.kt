package com.mindflip.dices.domain.model

import com.mindflip.dices.R

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Dice(
    val type: DiceType,
    var currentFaceIndex: Int,
    isSelectedInitial: Boolean,  // меняем конструктор, чтобы использовать делегат
    val diceFaces: Array<DiceFace>
) {

    var isSelected by mutableStateOf(isSelectedInitial)  // реактивное поле

    enum class DiceType {
        D6
    }

    companion object {
        fun create(type: DiceType): Dice = when(type) {
            DiceType.D6 -> {
                val faceImagesIDs: Array<Int> = getFaceImagesIdsD6()
                Dice(
                    type,
                    5,
                    false,
                    diceFaces = (1..6).map {
                        DiceFace(
                            DiceFace.FaceState.normal,
                            faceValue = it,
                            faceImageID = faceImagesIDs[it-1]
                        )
                    }.toTypedArray()
                )
            }
        }

        fun getFaceImagesIdsD6(): Array<Int> {
            return arrayOf(
                R.drawable.d6_1,
                R.drawable.d6_2,
                R.drawable.d6_3,
                R.drawable.d6_4,
                R.drawable.d6_5,
                R.drawable.d6_6
            )
        }
    }

    fun roll() {
        currentFaceIndex = (0 until diceFaces.size).random()
    }
}
