package com.robertzhao.adventofchristmastree

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdventOfChristmasTreeTheme {
                val state by viewModel.state.collectAsState()
                Scaffold(modifier = Modifier.fillMaxSize()) { contentPadding ->
                    when (state) {
                        is State.Simulation -> SimulationScreen(
                            simulationState = state as State.Simulation,
                            onValueChange = viewModel::updateSpeed,
                            modifier = Modifier.padding(contentPadding),
                            onTimeClick = { viewModel.start(it) },
                            onRetryClick = { viewModel.start(0, true) },
                            onPauseClick = viewModel::pause,
                            onPreviousClick = viewModel::onPreviousClicked,
                            onNextClick = viewModel::onNextClicked,
                        ) { viewModel.resume() }

                        is State.Start -> StartScreen { viewModel.start(0) }
                    }
                }
            }
        }
    }
}

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("2024 Advent of Code Day 14")
            Spacer(Modifier.padding(10.dp))
            Button(onClick) { Text("Start") }
        }
    }
}

@Composable
fun EditTimeScreen(
    currentTime: Int,
    modifier: Modifier = Modifier,
    onCancelClicked: () -> Unit,
    onOkClicked: (Int) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(10.dp))
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            var textFieldValue by remember { mutableStateOf(currentTime.toString()) }
            val errorMessage =
                "Integer from 0 to $SIMULATION_ITERATIONS, inclusive!"
            OutlinedTextField(
                value = textFieldValue,
                textStyle = TextStyle(color = Color.Black),
                onValueChange = { textFieldValue = it },
                label = { Text("Enter Time", color = Color.Black) }
            )
            textFieldValue.toIntOrNull()?.also { intValue ->
                if ((intValue in 0..SIMULATION_ITERATIONS).not()) {
                    Text(errorMessage, color = Color.Red)
                }
            } ?: Text(errorMessage, color = Color.Red)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                TextButton({
                    textFieldValue.toIntOrNull()?.also { intValue ->
                        if (intValue in 0..SIMULATION_ITERATIONS) {
                            onOkClicked(intValue)
                        }
                    }
                }) { Text("Ok", color = Color.Black) }
                TextButton(onCancelClicked) { Text("Cancel", color = Color.Black) }
            }
        }
    }
}

@Composable
fun SimulationScreen(
    simulationState: State.Simulation,
    modifier: Modifier = Modifier,
    onValueChange: (Float) -> Unit,
    onTimeClick: (Int) -> Unit,
    onRetryClick: () -> Unit,
    onPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onResumeClick: () -> Unit,
) {
    var isEditingTime by remember { mutableStateOf(false) }
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Grid(
                gridState = simulationState.grid,
            )
            TextButton(
                onClick = { isEditingTime = true },
                modifier = Modifier
                    .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
            ) { Text("Time: ${simulationState.t}", color = Color.Black) }
            if (simulationState.t == SIMULATION_ITERATIONS) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onPreviousClick) { Text("Previous") }
                    Button(onRetryClick) { Text("Run Again") }
                }
            } else {
                if (simulationState.paused) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (simulationState.t > 0) {
                            Button(onPreviousClick) { Text("Previous") }
                        }
                        Button(onResumeClick) { Text("Resume") }
                        Button(onNextClick) { Text("Next") }
                    }
                } else {
                    Button(onPauseClick) { Text("Pause") }
                }
            }
            SpeedSlider { onValueChange(it) }
        }
        if (isEditingTime) {
            EditTimeScreen(
                currentTime = simulationState.t,
                onCancelClicked = { isEditingTime = false },
            ) {
                isEditingTime = false
                onTimeClick(it)
            }
        }
    }
}

@Composable
fun Grid(gridState: List<List<Boolean>>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth(0.95f)
            .fillMaxHeight(0.65f)
            .border(BorderStroke(2.dp, Color.Red)),
    ) {
        gridState.forEach { gridRow ->
            GridRow(
                gridRow = gridRow,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
fun GridRow(gridRow: List<Boolean>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        gridRow.forEach { item ->
            Box(
                modifier = Modifier
                    .background(
                        if (item) {
                            Color.Green
                        } else {
                            Color.Transparent
                        }
                    )
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
fun SpeedSlider(
    modifier: Modifier = Modifier,
    onValueChange: (Float) -> Unit,
) {
    var sliderValue by remember { mutableFloatStateOf(0.5f) }
    Column(modifier.fillMaxWidth(0.9f)) {
        Slider(
            value = sliderValue,
            onValueChange = {
                sliderValue = it
                onValueChange(it)
            },
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("\uD83D\uDC22")
            Text("\uD83D\uDC07")
        }
    }
}
