package com.mindflip.dices.domain.model

class DiceFace(
    var faceState : FaceState,
    val faceValue : Int,
    val faceImageID : Int
) {
    enum class FaceState{
        normal,
        burnt,
        protected,
        blocked
    }
}