package com.robertzhao.adventofchristmastree

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val SPEED_UP_FACTOR = 500

class MainViewModel : ViewModel() {
    private val _delay: MutableStateFlow<Long> = MutableStateFlow(500)
    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Start)
    val state: StateFlow<State> = _state

    fun start(time: Int = 0, restart: Boolean = false) {
        val bots = INITIAL_BOTS.skipToTime(time)
        _state.update { currentState ->
            State.Simulation(
                grid = bots.toGrid(),
                bots = bots,
                paused = (currentState as? State.Simulation)?.paused == true && !restart,
                t = time,
            )
        }
        if (!(_state.value as State.Simulation).paused) {
            viewModelScope.launch {
                delay(20)
                simulate()
            }
        }
    }


    fun pause() {
        _state.update { currentState -> (currentState as State.Simulation).copy(paused = true) }
        viewModelScope.coroutineContext.cancelChildren()
    }

    fun resume() {
        _state.update { currentState -> (currentState as State.Simulation).copy(paused = false) }
        viewModelScope.launch {
            simulate()
        }
    }

    fun updateSpeed(newValue: Float) = _delay.update {
        val newSpeed = (SPEED_UP_FACTOR - newValue * SPEED_UP_FACTOR).toLong()
        if (newSpeed == 0L) {
            1
        } else {
            newSpeed
        }
    }

    fun onNextClicked() = viewModelScope.launch { nextState() }

    fun onPreviousClicked() = viewModelScope.launch { previousState() }

    private suspend fun nextState() {
        val currentBotState = (state.value as State.Simulation).bots
        val nextBotState = currentBotState.simulateSingleIteration {
            this.nextBotState()
        }
        _state.update { currentState ->
            (currentState as State.Simulation).copy(
                grid = nextBotState.toGrid(),
                bots = nextBotState,
                t = currentState.t + 1,
            )
        }
    }

    private suspend fun previousState() {
        val currentBotState = (state.value as State.Simulation).bots
        val previousBotState = currentBotState.simulateSingleIteration {
            this.toPreviousState()
        }
        _state.update { currentState ->
            (currentState as State.Simulation).copy(
                grid = previousBotState.toGrid(),
                bots = previousBotState,
                t = currentState.t - 1,
            )
        }
    }

    private suspend fun simulate() {
        while ((_state.value as State.Simulation).t < SIMULATION_ITERATIONS) {
            delay(_delay.value)
            nextState()
        }
        pause()
    }

    private suspend fun List<Bot>.simulateSingleIteration(
        followingState: Bot.() -> Bot
    ): List<Bot> {
        val currentState = this
        val nextBotState = currentState.toMutableList()
        flow {
            currentState.forEachIndexed { index, bot ->
                emit(index to bot.followingState())
            }
        }.collect { (index, bot) ->
            nextBotState[index] = bot
        }
        return nextBotState
    }

    private fun Bot.nextBotState(): Bot = this.copy(
        x = (x + dx).mod(WIDTH),
        y = (y + dy).mod(HEIGHT),
    )

    private fun Bot.toPreviousState(): Bot = this.copy(
        x = (x - dx).mod(WIDTH),
        y = (y - dy).mod(HEIGHT),
    )

    private fun List<Bot>.toGrid(): List<List<Boolean>> {
        val listToReturn = MutableList(HEIGHT) {
            MutableList(WIDTH) {
                false
            }
        }
        this.forEach { bot ->
            listToReturn[bot.y][bot.x] = true
        }
        return listToReturn
    }

    private fun List<Bot>.skipToTime(time: Int) = map { bot ->
        bot.copy(
            x = (bot.x + time * bot.dx).mod(WIDTH),
            y = (bot.y + time * bot.dy).mod(HEIGHT),
        )
    }
}