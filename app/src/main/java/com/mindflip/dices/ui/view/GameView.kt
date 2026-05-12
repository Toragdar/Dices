package com.mindflip.dices.ui.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mindflip.dices.domain.model.GameState
import com.mindflip.dices.domain.model.ResultCases
import com.mindflip.dices.ui.components.AiHandView
import com.mindflip.dices.ui.components.PlayerHandView
import com.mindflip.dices.ui.theme.DicesTheme
import com.mindflip.dices.viewmodel.GameViewModel

//TODO Настроить игровый цикл со сменой фаз игры и реализовать корректное поведение ИИ, добавить функцию определения победителя и окно конца игры

@Composable
fun GameScreen(modifier: Modifier = Modifier) {
    Log.d("BoardComposable", "Starting")

    val viewModel: GameViewModel = viewModel()

    val playerDices by viewModel.playerDices.collectAsState()
    val playerCurrentCombination by viewModel.playerCurrentCombination.collectAsState()
    val playerCurrentHP by viewModel.playerCurrentHP.collectAsState()

    val aiDices by viewModel.aiDices.collectAsState()
    val aiCurrentCombination by viewModel.aiCurrentCombination.collectAsState()
    val aiCurrentHP by viewModel.aiCurrentHP.collectAsState()

    val winner by viewModel.winner.collectAsState()

    val gameState by viewModel.gameState.collectAsState()

    val rollingPhaseMessage: String = "Please, choose dices to reroll"
    val resultPhaseWinnerMessage: String = "Winner: $winner"
    val resultPhaseDrawMessage: String = "Draw"

    Column {
        //region Половина противника
        Row(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)){

            //Счетчик очков/раундов
            Box(modifier = Modifier
                .background(Color.DarkGray)
                .fillMaxHeight()
                .weight(1f),
                contentAlignment = Alignment.Center
            ){
                Box(modifier=Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .background(Color.White)
                ){
                    //Временная переменная для результата
                    Text(modifier = Modifier
                        .padding(8.dp)
                        .background(Color.Transparent),
                        text = "AI's HP:\n$aiCurrentHP"
                    )
                }
            }
            //Рука противника
            Box(modifier = Modifier
                .background(Color.Black)
                .fillMaxHeight()
                .weight(2f)
            ){
                AiHandView(
                    aiDices)
            }
            //Выбор магии
            Box(modifier = Modifier
                .background(Color.DarkGray)
                .fillMaxHeight()
                .weight(1f))
        }
        //endregion

        //region Игровая панель
        Row(modifier = Modifier
            .fillMaxWidth()
            .weight(0.3f)
        ){
            Box(modifier = Modifier
                .background(Color.Black)
                .fillMaxHeight()
                .weight(1f),
                contentAlignment = Alignment.Center
            ){
                Box(modifier=Modifier
                    .background(Color.White)
                )
                Text(modifier = Modifier
                    .padding(4.dp)
                    .background(Color.White),
                    text = when (gameState){
                        GameState.PLAYER_CHOOSE -> rollingPhaseMessage
                        GameState.END_ROUND -> {
                            when (winner){
                                ResultCases.DRAW -> resultPhaseDrawMessage
                                else -> resultPhaseWinnerMessage
                            }
                        }
                        else -> "Something gone wrong"
                    })
            }
            Box(modifier = Modifier
                .background(Color.Black)
                .fillMaxHeight()
                .weight(1f),
                contentAlignment = Alignment.Center
            ){
                //временная кнопка броска костей
                if (gameState == GameState.PLAYER_CHOOSE){
                    Button(
                        onClick = {
                            viewModel.finishTurn()
                        }
                    ) {
                        Text("Roll")
                    }
                }
            }
            Box(modifier = Modifier
                .background(Color.Black)
                .fillMaxHeight()
                .weight(1f),
                contentAlignment = Alignment.Center
            ){
                //временная кнопка рестарта
                if (gameState == GameState.END_ROUND){
                    Button(
                        onClick = {
                            viewModel.startNextRound()
                        }
                    ) {
                        Text("Next Round")
                    }
                }
            }
        }
        //endregion

        //region Половина игрока
        Row(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)){

            //Счетчик очков/раундов
            Box(modifier = Modifier
                .background(Color.DarkGray)
                .fillMaxHeight()
                .weight(1f),
                contentAlignment = Alignment.Center
            ){
                Box(modifier=Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .background(Color.White)
                ){
                    //Временная переменная для результата
                    Text(modifier = Modifier
                        .padding(8.dp)
                        .background(Color.White),
                        text = "Player's HP:\n$playerCurrentHP"
                    )
                }
            }
            //Рука игрока
            Box(modifier = Modifier
                .background(Color.Black)
                .fillMaxHeight()
                .weight(2f)
            ){
                PlayerHandView(
                    playerDices,
                    onDiceClick = {index ->
                        if(gameState == GameState.PLAYER_CHOOSE){
                            viewModel.togglePlayerDiceSelection(index)
                        }
                    })
            }
            //Выбор магии
            Box(modifier = Modifier
                .background(Color.DarkGray)
                .fillMaxHeight()
                .weight(1f),
                contentAlignment = Alignment.Center
            ){

            }
        }
        //endregion
    }
}

//region превью
@Preview(
    showBackground = true,
    widthDp = 800,
    heightDp = 360
)
@Composable
fun BoardPreview() {
    DicesTheme {
        GameScreen()
    }
}
//endregion