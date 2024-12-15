package com.robertzhao.adventofchristmastree

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun StartPreview() {
    Scaffold { padding ->
        StartScreen(Modifier.padding(padding)) {}
    }
}

@Preview(showBackground = true)
@Composable
fun GridRowPreview() {
    Scaffold { padding ->
        GridRow(
            modifier = Modifier.padding(padding),
            gridRow = listOf(false, true, false)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GridPreview() {
    Scaffold { padding ->
        Grid(
            modifier = Modifier.padding(padding),
            gridState = listOf(
                listOf(false, true, false),
                listOf(true, false, true),
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SimulationPreview() {
    Scaffold { padding ->
        SimulationScreen(
            modifier = Modifier.padding(padding),
            simulationState = State.Simulation(
                grid = listOf(
                    listOf(false, true, false),
                    listOf(true, false, true),
                ),
                bots = emptyList(),
                paused = false,
                t = 7892,
            ),
            onValueChange = {},
            onTimeClick = {},
            onRetryClick = {},
            onNextClick = {},
            onPreviousClick = {},
            onPauseClick = {},
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun SimulationOngoingPreview() {
    Scaffold { padding ->
        SimulationScreen(
            modifier = Modifier.padding(padding),
            simulationState = State.Simulation(
                grid = listOf(
                    listOf(false, true, false),
                    listOf(true, false, true),
                ),
                bots = emptyList(),
                paused = true,
                t = 7891,
            ),
            onValueChange = {},
            onTimeClick = {},
            onRetryClick = {},
            onPauseClick = {},
            onPreviousClick = {},
            onNextClick = {},
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun SliderPreview() {
    Scaffold { padding ->
        SpeedSlider(Modifier.padding(padding)) {}
    }
}

@Preview(showBackground = true)
@Composable
fun EditTimePreview() {
    Scaffold { padding ->
        EditTimeScreen(
            currentTime = 69,
            modifier = Modifier.padding(padding),
            onCancelClicked = {},
        ) {}
    }
}



