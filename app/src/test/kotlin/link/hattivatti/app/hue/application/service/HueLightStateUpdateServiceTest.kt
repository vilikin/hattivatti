package link.hattivatti.app.hue.application.service

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import link.hattivatti.app.hue.application.port.driven.FindHueUserPort
import link.hattivatti.app.hue.application.port.driven.UpdateHueLightStatePort
import link.hattivatti.app.hue.domain.light.model.HueLightIdentifier
import link.hattivatti.app.hue.domain.light.model.HueLightState
import link.hattivatti.app.hue.domain.model.HueUserFixtures
import org.junit.jupiter.api.Test

class HueLightStateUpdateServiceTest {

    private val findHueUserPortMock = mockk<FindHueUserPort>()
    private val updateHueLightStatePortMock = mockk<UpdateHueLightStatePort>(relaxed = true)

    private val service = HueLightStateUpdateService(
        findHueUserPortMock,
        updateHueLightStatePortMock
    )

    @Test
    fun `should update hue light state of given user`() = runBlocking {
        val user = HueUserFixtures.user
        val hueLightIdentifier = HueLightIdentifier(123)
        val requestedHueLightState = HueLightState(
            on = true
        )

        coEvery {
            findHueUserPortMock.findHueUser(user.id)
        } returns user

        service.updateLightState(
            hueUserId = user.id,
            hueLightId = hueLightIdentifier,
            hueLightState = requestedHueLightState
        )

        coVerify {
            updateHueLightStatePortMock.updateLightState(
                hueUser = user,
                hueLightId = hueLightIdentifier,
                hueLightState = requestedHueLightState
            )
        }
    }
}
