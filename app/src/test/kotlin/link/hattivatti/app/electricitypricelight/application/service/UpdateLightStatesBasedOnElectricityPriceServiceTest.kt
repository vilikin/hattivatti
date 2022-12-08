package link.hattivatti.app.electricitypricelight.application.service

import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import link.hattivatti.app.common.color.RgbColor
import link.hattivatti.app.common.time.InstantTimeSource
import link.hattivatti.app.electricity.application.port.driven.ListElectricityPricesPort
import link.hattivatti.app.electricity.domain.model.ElectricityPriceForHour
import link.hattivatti.app.electricitypricelight.application.service.UpdateLightStatesBasedOnElectricityPriceService.Companion.STATIC_BRIGHTNESS
import link.hattivatti.app.electricitypricelight.application.service.UpdateLightStatesBasedOnElectricityPriceService.Companion.STATIC_TEMPORARY_LIGHT_ID
import link.hattivatti.app.electricitypricelight.domain.service.LightColorCalculator
import link.hattivatti.app.hue.application.port.driven.ListHueUsersPort
import link.hattivatti.app.hue.application.port.driven.UpdateHueLightStatePort
import link.hattivatti.app.hue.domain.light.model.Brightness
import link.hattivatti.app.hue.domain.light.model.HueLightIdentifier
import link.hattivatti.app.hue.domain.light.model.HueLightState
import link.hattivatti.app.hue.domain.user.model.*
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class UpdateLightStatesBasedOnElectricityPriceServiceTest {
    private val listElectricityPricesPortMock = mockk<ListElectricityPricesPort>()
    private val listHueUsersPortMock = mockk<ListHueUsersPort>()
    private val lightColorCalculator = mockk<LightColorCalculator>()
    private val updateHueLightStatePort = mockk<UpdateHueLightStatePort>(relaxed = true)
    private val instantTimeSource = mockk<InstantTimeSource>()

    private val service = UpdateLightStatesBasedOnElectricityPriceService(
        listElectricityPricesPortMock,
        listHueUsersPortMock,
        lightColorCalculator,
        updateHueLightStatePort,
        instantTimeSource
    )

    private val currentTime = Instant.parse("2022-12-08T19:05:00Z")

    private val outdatedElectricityPrice = ElectricityPriceForHour(
        startTime = Instant.parse("2022-12-08T18:00:00Z"),
        endTime = Instant.parse("2022-12-08T19:00:00Z"),
        centsPerMwh = 1
    )

    private val currentElectricityPrice = ElectricityPriceForHour(
        startTime = Instant.parse("2022-12-08T19:00:00Z"),
        endTime = Instant.parse("2022-12-08T20:00:00Z"),
        centsPerMwh = 2
    )

    private val futureElectricityPrice = ElectricityPriceForHour(
        startTime = Instant.parse("2022-12-08T20:00:00Z"),
        endTime = Instant.parse("2022-12-08T21:00:00Z"),
        centsPerMwh = 3
    )

    private val hueUser1 = HueUser(
        id = HueUserIdentifier(UUID.fromString("160c0728-5634-4ef6-9cfd-12fd068e2db5")),
        username = HueUsername("Username1"),
        tokens = TokenSet(
            accessToken = AccessToken("AccessToken1"),
            accessTokenExpiresAt = Instant.parse("2022-12-08T21:00:00Z"),
            refreshToken = RefreshToken("RefreshToken1")
        )
    )

    private val hueUser2 = HueUser(
        id = HueUserIdentifier(UUID.fromString("05c6fec3-e111-4f54-98e7-3456cbd26424")),
        username = HueUsername("Username2"),
        tokens = TokenSet(
            accessToken = AccessToken("AccessToken2"),
            accessTokenExpiresAt = Instant.parse("2022-12-08T21:00:00Z"),
            refreshToken = RefreshToken("RefreshToken2")
        )
    )

    @Test
    fun `should update light states of all users based on the current electricity price`() = runBlocking {
        every { instantTimeSource.now() } returns currentTime

        coEvery {
            listElectricityPricesPortMock.listElectricityPrices()
        } returns listOf(outdatedElectricityPrice, currentElectricityPrice, futureElectricityPrice)

        coEvery {
            listHueUsersPortMock.listHueUsers()
        } returns listOf(hueUser1, hueUser2)

        every {
            lightColorCalculator.calculateColorForElectricityPrice(currentElectricityPrice.centsPerMwh)
        } returns RgbColor.BLUE.toXyColor()

        service.updateLightStates()

        coVerifyAll {
            updateHueLightStatePort.updateLightState(
                hueUser = hueUser1,
                hueLightId = HueLightIdentifier(STATIC_TEMPORARY_LIGHT_ID),
                hueLightState = HueLightState(
                    on = true,
                    color = RgbColor.BLUE.toXyColor(),
                    brightness = Brightness(STATIC_BRIGHTNESS)
                )
            )

            updateHueLightStatePort.updateLightState(
                hueUser = hueUser2,
                hueLightId = HueLightIdentifier(STATIC_TEMPORARY_LIGHT_ID),
                hueLightState = HueLightState(
                    on = true,
                    color = RgbColor.BLUE.toXyColor(),
                    brightness = Brightness(STATIC_BRIGHTNESS)
                )
            )
        }
    }
}