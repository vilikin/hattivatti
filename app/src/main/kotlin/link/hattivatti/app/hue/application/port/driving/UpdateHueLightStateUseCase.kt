package link.hattivatti.app.hue.application.port.driving

import link.hattivatti.app.hue.domain.light.model.HueLightIdentifier
import link.hattivatti.app.hue.domain.light.model.HueLightState
import link.hattivatti.app.hue.domain.user.model.HueUserIdentifier

interface UpdateHueLightStateUseCase {
    fun updateLightState(
        hueUserId: HueUserIdentifier,
        hueLightIdentifier: HueLightIdentifier,
        hueLightState: HueLightState
    )
}
