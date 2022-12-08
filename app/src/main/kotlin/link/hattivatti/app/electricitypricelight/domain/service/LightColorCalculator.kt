package link.hattivatti.app.electricitypricelight.domain.service

import link.hattivatti.app.common.color.*
import org.springframework.stereotype.Component
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.roundToInt

interface ILightColorCalculator {
    fun calculateColorForElectricityPrice(centsPerMwh: Int): XyColor
}

@Component
class LightColorCalculator : ILightColorCalculator {
    override fun calculateColorForElectricityPrice(centsPerMwh: Int): XyColor {
        val normalizedPrice = min(centsPerMwh, 100000) / 100000f

        val gradient = MultiColorGradient(listOf(RgbColor.GREEN, RgbColor.BLUE, RgbColor.RED))

        return gradient.getColorAtPoint(normalizedPrice).toXyColor()
    }
}

data class RgbColorAsDecimals(
    val r: Float,
    val g: Float,
    val b: Float
) {
    fun toRgbColor() = RgbColor(
        r = (r * 255).roundToInt(),
        g = (g * 255).roundToInt(),
        b = (b * 255).roundToInt()
    )
}

fun RgbColor.adjustToDecimals(): RgbColorAsDecimals = RgbColorAsDecimals(
    r = this.r / 255f,
    g = this.g / 255f,
    b = this.b / 255f
)

data class TwoColorGradient(
    val colorA: RgbColor,
    val colorB: RgbColor
) {
    fun getColorAtPoint(point: Float): RgbColor {
        val adjustedA = colorA.adjustToDecimals()
        val adjustedB = colorB.adjustToDecimals()

        val r = adjustedA.r + (adjustedB.r - adjustedA.r) * point
        val g = adjustedA.g + (adjustedB.g - adjustedA.g) * point
        val b = adjustedA.b + (adjustedB.b - adjustedA.b) * point

        return RgbColorAsDecimals(r, g, b).toRgbColor()
    }
}

data class MultiColorGradient(
    val colors: List<RgbColor>
) {
    init {
        check(colors.size >= 3)
    }

    fun getColorAtPoint(point: Float): RgbColor {
        val twoColorGradientSize = 1f / (colors.size - 1)
        val startingIndexOfTwoColorGradientAtPoint = floor(point / twoColorGradientSize).toInt()

        if (startingIndexOfTwoColorGradientAtPoint == colors.size - 1) {
            return colors[colors.size - 1]
        }

        val twoColorGradient = TwoColorGradient(
            colors[startingIndexOfTwoColorGradientAtPoint],
            colors[startingIndexOfTwoColorGradientAtPoint + 1]
        )

        val pointInTwoColorGradient = (point / twoColorGradientSize) % 1
        return twoColorGradient.getColorAtPoint(pointInTwoColorGradient)
    }
}
