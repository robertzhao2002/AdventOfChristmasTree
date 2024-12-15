package com.robertzhao.adventofchristmastree

import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun ComponentActivity.AdventOfChristmasTreeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) {
            dynamicDarkColorScheme(baseContext)
        } else {
            dynamicLightColorScheme(baseContext)
        },
        content = content
    )
}