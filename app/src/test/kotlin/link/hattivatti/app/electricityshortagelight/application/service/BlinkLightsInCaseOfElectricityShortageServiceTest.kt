package link.hattivatti.app.electricityshortagelight.application.service

import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import link.hattivatti.app.electricity.application.port.driven.GetElectricityShortageStatusPort
import link.hattivatti.app.electricity.domain.model.ElectricityShortageStatus
import link.hattivatti.app.electricitypricelight.application.service.UpdateLightStatesBasedOnElectricityPriceService
import link.hattivatti.app.hue.application.port.driven.ListHueUsersPort
import link.hattivatti.app.hue.application.port.driven.UpdateHueLightStatePort
import link.hattivatti.app.hue.domain.light.model.HueAlertMode
import link.hattivatti.app.hue.domain.light.model.HueLightIdentifier
import link.hattivatti.app.hue.domain.light.model.HueLightState
import link.hattivatti.app.hue.domain.user.model.*
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class BlinkLightsInCaseOfElectricityShortageServiceTest {
    private val getElectricityShortageStatusPortMock = mockk<GetElectricityShortageStatusPort>()
    private val listHueUsersPortMock = mockk<ListHueUsersPort>()
    private val updateHueLightStatePort = mockk<UpdateHueLightStatePort>(relaxed = true)

    private val service = BlinkLightsInCaseOfElectricityShortageService(
        getElectricityShortageStatusPortMock,
        listHueUsersPortMock,
        updateHueLightStatePort,
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
    fun `should blink lights of all users in case of electricity shortage`() = runBlocking {
        coEvery {
            getElectricityShortageStatusPortMock.getElectricityShortageStatus()
        } returns ElectricityShortageStatus.ELECTRICITY_SHORTAGE

        coEvery {
            listHueUsersPortMock.listHueUsers()
        } returns listOf(hueUser1, hueUser2)

        service.blinkLightsInCaseOfElectricityShortage()

        coVerifyAll {
            updateHueLightStatePort.updateLightState(
                hueUser = hueUser1,
                hueLightId = HueLightIdentifier(UpdateLightStatesBasedOnElectricityPriceService.STATIC_TEMPORARY_LIGHT_ID),
                hueLightState = HueLightState(
                    alert = HueAlertMode.BLINK_MULTIPLE_TIMES
                )
            )

            updateHueLightStatePort.updateLightState(
                hueUser = hueUser2,
                hueLightId = HueLightIdentifier(UpdateLightStatesBasedOnElectricityPriceService.STATIC_TEMPORARY_LIGHT_ID),
                hueLightState = HueLightState(
                    alert = HueAlertMode.BLINK_MULTIPLE_TIMES
                )
            )
        }
    }
}