package link.hattivatti.app.hue.adapter.hueremoteapi.dto

data class LightStateDto(
    val on: Boolean? = null,
    val xy: Array<Double>? = null,
    val bri: Int? = null,
    val alert: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LightStateDto

        if (on != other.on) return false
        if (xy != null) {
            if (other.xy == null) return false
            if (!xy.contentEquals(other.xy)) return false
        } else if (other.xy != null) return false
        if (bri != other.bri) return false
        if (alert != other.alert) return false

        return true
    }

    override fun hashCode(): Int {
        var result = on?.hashCode() ?: 0
        result = 31 * result + (xy?.contentHashCode() ?: 0)
        result = 31 * result + (bri ?: 0)
        result = 31 * result + (alert?.hashCode() ?: 0)
        return result
    }

}
