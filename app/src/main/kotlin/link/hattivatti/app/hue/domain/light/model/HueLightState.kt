package link.hattivatti.app.hue.domain.light.model

import link.hattivatti.app.common.color.XyColor

data class HueLightState(
    val on: Boolean? = null,
    val color: XyColor? = null,
    val brightness: Brightness? = null
)
