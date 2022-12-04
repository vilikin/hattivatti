package link.hattivatti.app.hue.application.port.driven

import link.hattivatti.app.hue.domain.light.model.HueLightState
import link.hattivatti.app.hue.domain.light.model.HueLightIdentifier
import link.hattivatti.app.hue.domain.user.model.HueUser

interface UpdateHueLightStatePort {
    suspend fun updateLightState(
        hueUser: HueUser,
        hueLightId: HueLightIdentifier,
        hueLightState: HueLightState
    )
}
