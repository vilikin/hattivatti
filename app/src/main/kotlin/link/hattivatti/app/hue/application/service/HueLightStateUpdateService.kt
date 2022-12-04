package link.hattivatti.app.hue.application.service

import link.hattivatti.app.hue.application.port.driven.FindHueUserPort
import link.hattivatti.app.hue.application.port.driven.UpdateHueLightStatePort
import link.hattivatti.app.hue.application.port.driving.UpdateHueLightStateUseCase
import link.hattivatti.app.hue.domain.light.model.HueLightIdentifier
import link.hattivatti.app.hue.domain.light.model.HueLightState
import link.hattivatti.app.hue.domain.user.model.HueUserIdentifier
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class HueLightStateUpdateService(
    private val findHueUserPort: FindHueUserPort,
    private val updateHueLightStatePort: UpdateHueLightStatePort
): UpdateHueLightStateUseCase {
    private val logger = LoggerFactory.getLogger(HueLightStateUpdateService::class.java)

    override suspend fun updateLightState(
        hueUserId: HueUserIdentifier,
        hueLightId: HueLightIdentifier,
        hueLightState: HueLightState
    ) {
        logger.info("Finding Hue user ${hueUserId.id}")
        val hueUser = findHueUserPort.findHueUser(hueUserId)

        logger.info("Updating light ${hueLightId.value} state of Hue user ${hueUserId.id}")
        updateHueLightStatePort.updateLightState(hueUser, hueLightId, hueLightState)
    }
}
