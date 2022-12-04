package link.hattivatti.app.hue.application.port.driven

import link.hattivatti.app.hue.domain.model.light.HueLightState
import link.hattivatti.app.hue.domain.model.light.HueLightIdentifier
import link.hattivatti.app.hue.domain.model.user.HueUser

interface UpdateHueLightStatePort {
    fun updateLightState(
        hueUser: HueUser,
        hueLightId: HueLightIdentifier,
        hueLightState: HueLightState
    )
}
