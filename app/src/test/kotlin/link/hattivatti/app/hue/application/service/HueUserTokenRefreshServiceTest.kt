package link.hattivatti.app.hue.application.service

import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import link.hattivatti.app.hue.application.port.driven.ExchangeHueUserRefreshTokenForTokensPort
import link.hattivatti.app.hue.application.port.driven.ListHueUsersPort
import link.hattivatti.app.hue.application.port.driven.SaveHueUserPort
import link.hattivatti.app.hue.domain.user.model.*
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.Instant
import java.util.*

class HueUserTokenRefreshServiceTest {

    private val listHueUsersPortMock = mockk<ListHueUsersPort>()
    private val exchangeHueUserRefreshTokenForTokensPortMock = mockk<ExchangeHueUserRefreshTokenForTokensPort>()
    private val saveHueUsersPortMock = mockk<SaveHueUserPort>(relaxed = true)

    private val service = HueUserTokenRefreshService(
        listHueUsersPortMock,
        exchangeHueUserRefreshTokenForTokensPortMock,
        saveHueUsersPortMock
    )

    @Test
    fun `should refresh tokens of every Hue user`() = runBlocking<Unit> {
        val hueUsers = listOf(
            HueUser(
                id = HueUserIdentifier(UUID.fromString("60a74a1d-5b20-4f6f-a1c3-ddb87b297973")),
                username = HueUsername("1"),
                tokens = TokenSet(
                    accessToken = AccessToken("oldAccessToken1"),
                    accessTokenExpiresAt = Instant.now(),
                    refreshToken = RefreshToken("oldRefreshToken1")
                )
            ),
            HueUser(
                id = HueUserIdentifier(UUID.fromString("d3082cba-61ce-4621-9504-d29765839837")),
                username = HueUsername("2"),
                tokens = TokenSet(
                    accessToken = AccessToken("oldAccessToken2"),
                    accessTokenExpiresAt = Instant.now(),
                    refreshToken = RefreshToken("oldRefreshToken2")
                )
            )
        )

        val newTokenSet1 = TokenSet(
            accessToken = AccessToken("newAccessToken1"),
            accessTokenExpiresAt = Instant.now().plus(Duration.ofDays(7)),
            refreshToken = RefreshToken("newRefreshToken1")
        )

        val newTokenSet2 = TokenSet(
            accessToken = AccessToken("newAccessToken2"),
            accessTokenExpiresAt = Instant.now().plus(Duration.ofDays(7)),
            refreshToken = RefreshToken("newRefreshToken2")
        )

        coEvery {
            listHueUsersPortMock.listHueUsers()
        } returns hueUsers

        coEvery {
            exchangeHueUserRefreshTokenForTokensPortMock.exchangeHueUserRefreshTokenForTokens(
                hueUsers[0].tokens.refreshToken
            )
        } returns newTokenSet1

        coEvery {
            exchangeHueUserRefreshTokenForTokensPortMock.exchangeHueUserRefreshTokenForTokens(
                hueUsers[1].tokens.refreshToken
            )
        } returns newTokenSet2

        service.refreshAllHueUserTokens()

        coVerifyAll {
            saveHueUsersPortMock.saveHueUser(
                hueUsers[0].copy(
                    tokens = newTokenSet1
                )
            )
            saveHueUsersPortMock.saveHueUser(
                hueUsers[1].copy(
                    tokens = newTokenSet2
                )
            )
        }
    }
}
