package link.hattivatti.app.hue.domain.model.light

data class HueLightState(
    val on: Boolean? = null,
    val hue: Hue? = null,
    val saturation: Saturation? = null,
    val brightness: Brightness? = null
)
