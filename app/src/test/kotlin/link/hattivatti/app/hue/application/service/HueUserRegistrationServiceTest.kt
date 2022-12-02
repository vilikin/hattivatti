package link.hattivatti.app.hue.application.service

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import link.hattivatti.app.common.uuid.UuidSource
import link.hattivatti.app.hue.application.port.driven.ExchangeHueUserAuthorizationCodeForTokensPort
import link.hattivatti.app.hue.application.port.driven.SaveHueUserPort
import link.hattivatti.app.hue.application.port.driven.SetupHueUsernamePort
import link.hattivatti.app.hue.domain.model.HueUserFixtures
import link.hattivatti.app.hue.domain.model.user.AuthorizationCode
import link.hattivatti.app.hue.domain.model.user.HueUserIdentifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class HueUserRegistrationServiceTest {
    private val exchangeHueUserAuthorizationCodeForTokensPortMock = mockk<ExchangeHueUserAuthorizationCodeForTokensPort>()
    private val setupHueUsernamePortMock = mockk<SetupHueUsernamePort>()
    private val saveHueUserPortMock = mockk<SaveHueUserPort>(relaxed = true)
    private val uuidSourceMock = mockk<UuidSource>()

    private val service = HueUserRegistrationService(
        exchangeHueUserAuthorizationCodeForTokensPort = exchangeHueUserAuthorizationCodeForTokensPortMock,
        setupHueUsernamePort = setupHueUsernamePortMock,
        saveHueUserPort = saveHueUserPortMock,
        uuidSource = uuidSourceMock
    )

    @Test
    fun `should register new Hue user using given authorization code`() = runBlocking<Unit> {
        val authorizationCode = AuthorizationCode("testAuthorizationCode")

        val expectedHueUserId = UUID.fromString("06f52c38-0a13-4b93-95d8-74225a47e1ad")
        val expectedHueUser = HueUserFixtures.user.copy(
            id = HueUserIdentifier(expectedHueUserId)
        )

        every { uuidSourceMock.uuid() } returns expectedHueUserId

        coEvery {
            exchangeHueUserAuthorizationCodeForTokensPortMock.exchangeHueUserAuthorizationCodeForTokens(
                authorizationCode
            )
        } returns expectedHueUser.tokens

        coEvery {
            setupHueUsernamePortMock.setupHueUsername(expectedHueUser.tokens.accessToken)
        } returns expectedHueUser.username

        val actualUser = service.registerHueUser(authorizationCode)

        coVerify {
            saveHueUserPortMock.saveHueUser(expectedHueUser)
        }

        assertThat(actualUser).isEqualTo(expectedHueUser)
    }
}
