package link.hattivatti.app.hue.adapter.hueremoteapi.dto

data class LightStateDto(
    val on: Boolean? = null,
    val hue: Int? = null,
    val saturation: Int? = null,
    val brightness: Int? = null
)
