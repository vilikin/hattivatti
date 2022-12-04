package link.hattivatti.app.hue.domain.model.light

data class HueLightState(
    val on: Boolean?,
    val hue: Hue?,
    val saturation: Saturation?,
    val brightness: Brightness?
)
