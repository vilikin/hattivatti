package link.hattivatti.app.hue.domain.model.user

import java.time.Instant

data class TokenSet(
    val accessToken: AccessToken,
    val accessTokenExpiresAt: Instant,
    val refreshToken: RefreshToken
)
