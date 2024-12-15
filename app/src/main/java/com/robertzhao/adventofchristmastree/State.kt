package com.robertzhao.adventofchristmastree

sealed interface State {
    data object Start : State
    data class Simulation(
        val grid: List<List<Boolean>>,
        val bots: List<Bot>,
        val paused: Boolean,
        val t: Int,
    ) : State
}