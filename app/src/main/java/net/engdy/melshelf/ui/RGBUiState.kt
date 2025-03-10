package net.engdy.melshelf.ui

import net.engdy.melshelf.model.RGBColor

data class RGBUiState(
    val color: RGBColor = RGBColor(red = 0, green = 0, blue = 0),
    val isConnected: Boolean = false,
    val ipAddress: String = ""
)