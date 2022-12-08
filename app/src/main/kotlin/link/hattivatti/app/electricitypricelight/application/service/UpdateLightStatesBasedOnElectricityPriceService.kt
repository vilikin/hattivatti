package link.hattivatti.app.electricitypricelight.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import link.hattivatti.app.common.time.InstantTimeSource
import link.hattivatti.app.electricity.application.port.driven.ListElectricityPricesPort
import link.hattivatti.app.electricitypricelight.application.port.driving.UpdateLightStatesBasedOnElectricityPriceUseCase
import link.hattivatti.app.electricitypricelight.domain.service.ILightColorCalculator
import link.hattivatti.app.hue.application.port.driven.ListHueUsersPort
import link.hattivatti.app.hue.application.port.driven.UpdateHueLightStatePort
import link.hattivatti.app.hue.domain.light.model.Brightness
import link.hattivatti.app.hue.domain.light.model.HueLightIdentifier
import link.hattivatti.app.hue.domain.light.model.HueLightState
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.temporal.ChronoUnit

@Component
class UpdateLightStatesBasedOnElectricityPriceService(
    private val listElectricityPricesPort: ListElectricityPricesPort,
    private val listHueUsersPort: ListHueUsersPort,
    private val lightColorCalculator: ILightColorCalculator,
    private val updateHueLightStatePort: UpdateHueLightStatePort,
    private val instantTimeSource: InstantTimeSource
) : UpdateLightStatesBasedOnElectricityPriceUseCase {
    private val logger = LoggerFactory.getLogger(UpdateLightStatesBasedOnElectricityPriceService::class.java)

    companion object {
        internal const val STATIC_TEMPORARY_LIGHT_ID = 13
        internal const val STATIC_BRIGHTNESS = 70
    }

    override suspend fun updateLightStates() = coroutineScope {
        logger.info("Finding current electricity price")
        val currentPrice = getCurrentElectricityPrice()

        logger.info("Calculating color for electricity price of $currentPrice cents/mwh")
        val colorForPrice = lightColorCalculator.calculateColorForElectricityPrice(currentPrice)

        logger.info("Listing all Hue users for light state updates")
        val hueUsers = listHueUsersPort.listHueUsers()

        hueUsers.map { user ->
            launch {
                logger.info(
                    "Updating light with id $STATIC_TEMPORARY_LIGHT_ID of Hue user ${user.id} to have color " +
                            "$colorForPrice with brightness $STATIC_BRIGHTNESS"
                )

                updateHueLightStatePort.updateLightState(
                    hueUser = user,
                    hueLightId = HueLightIdentifier(STATIC_TEMPORARY_LIGHT_ID),
                    hueLightState = HueLightState(
                        on = true,
                        color = colorForPrice,
                        brightness = Brightness(STATIC_BRIGHTNESS)
                    )
                )
            }
        }
            .forEach { it.join() }

        logger.info("Done updating all light states")
    }

    private suspend fun getCurrentElectricityPrice(): Int {
        val electricityPrices = listElectricityPricesPort.listElectricityPrices()

        val now = instantTimeSource.now()
        val currentHour = now.truncatedTo(ChronoUnit.HOURS)

        return electricityPrices
            .find { it.startTime == currentHour }
            ?.centsPerMwh
            ?: throw CurrentPriceUnknownException()
    }

    class CurrentPriceUnknownException : RuntimeException("Current price of electricity is not known")
}
