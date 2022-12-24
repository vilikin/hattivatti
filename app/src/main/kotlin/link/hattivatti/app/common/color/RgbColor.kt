package link.hattivatti.app.common.color

import kotlin.math.pow

data class RgbColor(
    val r: Int,
    val g: Int,
    val b: Int
) {
    companion object {
        val RED = RgbColor(255, 0, 0)
        val GREEN = RgbColor(0, 255, 0)
        val BLUE = RgbColor(0, 0, 255)
    }

    fun toXyColor(): XyColor {
        val rDecimal = r.toFloat() / 255
        val gDecimal = g.toFloat() / 255
        val bDecimal = b.toFloat() / 255

        val r = applyGammaCorrection(rDecimal)
        val g = applyGammaCorrection(gDecimal)
        val b = applyGammaCorrection(bDecimal)

        val x = r * 0.4124 + g * 0.3576 + b * 0.1805
        val y = r * 0.2126 + g * 0.7152 + b * 0.0722
        val z = r * 0.0193 + g * 0.1192 + b * 0.9505

        return XyColor(
            x = x / (x + y + z),
            y = y / (x + y + z)
        )
    }

    private fun applyGammaCorrection(component: Float): Float {
        return if (component > 0.04045f) {
            component / 12.92f
        } else {
            ((component + 0.055f) / (1.0f + 0.055f)).pow(2.4f)
        }
    }
}
