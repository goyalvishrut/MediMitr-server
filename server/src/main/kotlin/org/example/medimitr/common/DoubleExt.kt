package org.example.medimitr.common

import kotlin.math.pow
import kotlin.math.roundToInt

private fun Double.roundToDecimalPlaces(decimals: Int): Double {
    val factor = 10.0.pow(decimals)
    return (this * factor).roundToInt() / factor
}

fun Double.roundToTwoDecimalPlaces(): Double = roundToDecimalPlaces(decimals = 2)
