package link.hattivatti.app.electricityshortagelight.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import link.hattivatti.app.electricity.application.port.driven.GetElectricityShortageStatusPort
import link.hattivatti.app.electricity.domain.model.ElectricityShortageStatus.ELECTRICITY_SHORTAGE_POSSIBLE
import link.hattivatti.app.electricity.domain.model.ElectricityShortageStatus.NORMAL
import link.hattivatti.app.electricitypricelight.application.service.UpdateLightStatesBasedOnElectricityPriceService.Companion.STATIC_TEMPORARY_LIGHT_ID
import link.hattivatti.app.electricityshortagelight.application.port.driving.BlinkLightsInCaseOfElectricityShortageUseCase
import link.hattivatti.app.hue.application.port.driven.ListHueUsersPort
import link.hattivatti.app.hue.application.port.driven.UpdateHueLightStatePort
import link.hattivatti.app.hue.domain.light.model.HueAlertMode
import link.hattivatti.app.hue.domain.light.model.HueLightIdentifier
import link.hattivatti.app.hue.domain.light.model.HueLightState
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class BlinkLightsInCaseOfElectricityShortageService(
    private val getElectricityShortageStatusPort: GetElectricityShortageStatusPort,
    private val listHueUsersPort: ListHueUsersPort,
    private val updateHueLightStatePort: UpdateHueLightStatePort,
) : BlinkLightsInCaseOfElectricityShortageUseCase {

    private val logger = LoggerFactory.getLogger(BlinkLightsInCaseOfElectricityShortageService::class.java)

    override suspend fun blinkLightsInCaseOfElectricityShortage() = coroutineScope {
        logger.info("Getting current electricity shortage status")
        val electricityShortageStatus = getElectricityShortageStatusPort.getElectricityShortageStatus()

        logger.info("Electricity shortage status is: ${electricityShortageStatus.name}")

        if (electricityShortageStatus in listOf(NORMAL, ELECTRICITY_SHORTAGE_POSSIBLE)) {
            return@coroutineScope
        }

        val hueUsers = listHueUsersPort.listHueUsers()

        hueUsers.map { user ->
            launch {
                logger.info(
                    "Updating light with id $STATIC_TEMPORARY_LIGHT_ID of Hue user ${user.id} to blink multiple times"
                )

                updateHueLightStatePort.updateLightState(
                    hueUser = user,
                    hueLightId = HueLightIdentifier(STATIC_TEMPORARY_LIGHT_ID),
                    hueLightState = HueLightState(alert = HueAlertMode.BLINK_MULTIPLE_TIMES)
                )
            }
        }
            .forEach { it.join() }
    }
}
