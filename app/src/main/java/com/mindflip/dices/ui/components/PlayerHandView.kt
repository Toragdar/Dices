package com.mindflip.dices.ui.components

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mindflip.dices.domain.model.Dice
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mindflip.dices.ui.theme.PlayerHandBackground
import com.mindflip.dices.ui.theme.TestedBackground

@Composable
fun PlayerHandView(
    dices: Array<Dice>,
    onDiceClick: (Int) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(PlayerHandBackground)
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
   ) {
        Log.d("PlayerHandView", "isStarting")
        dices.forEachIndexed { index, dice ->
            val offsetY by animateDpAsState(
                targetValue = if (dice.isSelected) (-12).dp else 0.dp,
                label = ""
            )
            Image(
                painter = painterResource(id = dice.diceFaces[dice.currentFaceIndex].faceImageID),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .offset(y = offsetY)
                    .weight(1f)
                    .background(TestedBackground)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null, // отключаем ripple
                        onClick = { onDiceClick(index) } // вызываем событие
                    )
            )
        }
        Log.d("PlayerHandView", "End")
   }
}
