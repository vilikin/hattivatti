package link.hattivatti.colorlab

import kotlinx.coroutines.runBlocking
import link.hattivatti.app.common.color.RgbColor
import link.hattivatti.app.common.time.CurrentInstantTimeSource
import link.hattivatti.app.hue.adapter.hueremoteapi.HueRemoteApiAdapter
import link.hattivatti.app.hue.domain.light.model.Brightness
import link.hattivatti.app.hue.domain.light.model.HueLightIdentifier
import link.hattivatti.app.hue.domain.light.model.HueLightState
import link.hattivatti.app.hue.domain.user.model.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

@Disabled
class ColorLabTest {
    @Test
    fun `test colors`() = runBlocking {
        val baseUrl = "https://api.meethue.com"
        val username = HueUsername(System.getenv("HUE_USERNAME"))
        val accessToken = AccessToken(System.getenv("HUE_ACCESS_TOKEN"))
        val lightId = HueLightIdentifier(13)

        val hueUser = HueUser(
            id = HueUserIdentifier(UUID.randomUUID()),
            username = username,
            tokens = TokenSet(
                accessToken = accessToken,
                accessTokenExpiresAt = Instant.now().plusSeconds(10),
                refreshToken = RefreshToken("abc")
            )
        )

        val hueRemoteApiAdapter = HueRemoteApiAdapter(
            baseUrl = baseUrl,
            clientId = "abc",
            clientSecret = "abc",
            CurrentInstantTimeSource()
        )

        val color = RgbColor.BLUE.toXyColor()

        val hueLightState = HueLightState(
            color = color,
            brightness = Brightness(70)
        )

        hueRemoteApiAdapter.updateLightState(hueUser, lightId, hueLightState)
    }
}
