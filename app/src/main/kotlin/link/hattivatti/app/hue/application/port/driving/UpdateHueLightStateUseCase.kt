package link.hattivatti.app.hue.application.port.driving

import link.hattivatti.app.hue.domain.model.light.HueLightIdentifier
import link.hattivatti.app.hue.domain.model.light.HueLightState
import link.hattivatti.app.hue.domain.model.user.HueUserIdentifier

interface UpdateHueLightStateUseCase {
    fun updateLightState(
        hueUserId: HueUserIdentifier,
        hueLightIdentifier: HueLightIdentifier,
        hueLightState: HueLightState
    )
}
