package link.hattivatti.app.hue.adapter.hueremoteapi

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.mockserver.model.HttpRequest.request
import link.hattivatti.app.common.time.InstantTimeSource
import link.hattivatti.app.hue.domain.model.user.AccessToken
import link.hattivatti.app.hue.domain.model.user.AuthorizationCode
import link.hattivatti.app.hue.domain.model.user.RefreshToken
import org.assertj.core.api.Assertions.assertThat
import link.hattivatti.app.testing.MockServerTest
import org.junit.jupiter.api.Test
import org.mockserver.model.HttpResponse.response
import org.mockserver.model.MediaType
import java.time.Instant
import link.hattivatti.app.hue.domain.model.user.TokenSet

class HueRemoteApiAdapterTest : MockServerTest() {

    private val clientId = "placeholder_client_id"
    private val clientSecret = "placeholder_client_secret"
    private val instantTimeSourceMock = mockk<InstantTimeSource>()

    private val hueRemoteApiAdapter = HueRemoteApiAdapter(
        baseUrl = mockServerContainer.endpoint,
        clientId = clientId,
        clientSecret = clientSecret,
        instantTimeSource = instantTimeSourceMock
    )

    @Test
    fun `should exchange authorization code for token set`() = runBlocking<Unit> {
        every {
            instantTimeSourceMock.now()
        } returns Instant.parse("2022-12-01T22:00:00Z")

        mockServerClient.`when`(
            request()
                .withMethod("POST")
                .withPath("/v2/oauth2/token")
                .withHeader(
                    "Authorization",
                    "Basic cGxhY2Vob2xkZXJfY2xpZW50X2lkOnBsYWNlaG9sZGVyX2NsaWVudF9zZWNyZXQ="
                )
                .withBody("grant_type=authorization_code&code=test")
        ).respond(
            response()
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(
                    """
                    {
                    	"access_token": "testAccessToken",
                    	"expires_in": 604800,
                    	"refresh_token": "testRefreshToken",
                    	"token_type": "bearer"
                    }
                """
                )
        )

        val result = hueRemoteApiAdapter.exchangeHueUserAuthorizationCodeForTokens(AuthorizationCode("test"))

        assertThat(result).isEqualTo(
            TokenSet(
                accessToken = AccessToken("testAccessToken"),
                accessTokenExpiresAt = Instant.parse("2022-12-08T22:00:00Z"),
                refreshToken = RefreshToken("testRefreshToken")
            )
        )
    }
}