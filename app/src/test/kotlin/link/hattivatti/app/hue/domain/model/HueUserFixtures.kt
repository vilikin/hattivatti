package link.hattivatti.app.hue.domain.model

import link.hattivatti.app.hue.domain.model.user.*
import java.time.Instant
import java.util.*

object HueUserFixtures {
    val user = HueUser(
        id = HueUserIdentifier(UUID.fromString("b59fd1a6-861f-4f27-a049-44612c4cf281")),
        username = HueUsername("testUsername"),
        tokens = TokenSet(
            accessToken = AccessToken("testAccessToken"),
            accessTokenExpiresAt = Instant.parse("2022-12-24T00:00:00Z"),
            refreshToken = RefreshToken("testRefreshToken")
        )
    )
}
