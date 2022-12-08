package link.hattivatti.app.hue.adapter.hueremoteapi

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import link.hattivatti.app.common.color.XyColor
import link.hattivatti.app.common.time.InstantTimeSource
import link.hattivatti.app.hue.domain.model.HueUserFixtures
import link.hattivatti.app.hue.domain.light.model.HueLightIdentifier
import link.hattivatti.app.hue.domain.light.model.HueLightState
import link.hattivatti.app.hue.domain.user.model.*
import link.hattivatti.app.testing.MockServerTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.mockserver.model.JsonBody
import org.mockserver.model.MediaType
import java.time.Instant

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

    @Test
    fun `should exchange refresh token for token set`() = runBlocking<Unit> {
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
                .withBody("grant_type=refresh_token&refresh_token=test")
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

        val result = hueRemoteApiAdapter.exchangeHueUserRefreshTokenForTokens(RefreshToken("test"))

        assertThat(result).isEqualTo(
            TokenSet(
                accessToken = AccessToken("testAccessToken"),
                accessTokenExpiresAt = Instant.parse("2022-12-08T22:00:00Z"),
                refreshToken = RefreshToken("testRefreshToken")
            )
        )
    }

    @Test
    fun `should setup hue username`() = runBlocking<Unit> {
        mockServerClient.`when`(
            request()
                .withMethod("PUT")
                .withPath("/route/api/0/config")
                .withHeader(
                    "Authorization",
                    "Bearer test"
                )
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody("""{"linkbutton":true}""")
        ).respond(
            response()
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(
                    """
                    [
                        {
                            "success": {
                                "/config/linkbutton": true
                            }
                        }
                    ]
                    """
                )
        )

        mockServerClient.`when`(
            request()
                .withMethod("POST")
                .withPath("/route/api")
                .withHeader(
                    "Authorization",
                    "Bearer test"
                )
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody("""{"devicetype":"hattivatti"}""")
        ).respond(
            response()
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(
                    """
                    [
                        {
                            "success": {
                                "username": "testUsername"
                            }
                        }
                    ]
                    """
                )
        )

        val result = hueRemoteApiAdapter.setupHueUsername(AccessToken("test"))

        assertThat(result).isEqualTo(HueUsername("testUsername"))
    }

    @Test
    fun `should update light state`() = runBlocking<Unit> {
        val hueLightId = HueLightIdentifier(123)
        val hueUser = HueUserFixtures.user

        val expectation = mockServerClient.`when`(
            request()
                .withMethod("PUT")
                .withPath("/route/api/${hueUser.username.username}/lights/${hueLightId.value}/state")
                .withHeader(
                    "Authorization",
                    "Bearer ${hueUser.tokens.accessToken.token}"
                )
                .withBody(JsonBody(
                    """
                    {
                        "on": true,
                        "xy": [0.1, 0.2]
                    }
                    """
                ))
        ).respond(
            response()
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(
                    """
                    [
                        {
                            "success": {
                                "/lights/123/state/on": true
                            }
                        },
                        {
                            "success": {
                                "/lights/123/state/xy": [0.1, 0.2]
                            }
                        }
                    ]
                    """
                )
        )

        hueRemoteApiAdapter.updateLightState(
            hueUser = hueUser,
            hueLightId = hueLightId,
            hueLightState = HueLightState(
                on = true,
                color = XyColor(x = 0.1, y = 0.2)
            )
        )

        mockServerClient.verify(expectation.first().id)
    }
}