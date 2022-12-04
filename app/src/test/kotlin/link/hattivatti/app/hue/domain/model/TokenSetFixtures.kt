package link.hattivatti.app.hue.domain.model

import link.hattivatti.app.hue.domain.user.model.AccessToken
import link.hattivatti.app.hue.domain.user.model.RefreshToken
import link.hattivatti.app.hue.domain.user.model.TokenSet
import java.time.Instant

object TokenSetFixtures {
    val tokenSet = TokenSet(
        accessToken = AccessToken("testAccessToken"),
        accessTokenExpiresAt = Instant.parse("2022-12-24T00:00:00Z"),
        refreshToken = RefreshToken("testRefreshToken")
    )
}
