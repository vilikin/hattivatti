package link.hattivatti.app.hue.adapter.hueremoteapi.dto

import com.fasterxml.jackson.annotation.JsonProperty
import link.hattivatti.app.hue.domain.user.model.AccessToken
import link.hattivatti.app.hue.domain.user.model.RefreshToken
import link.hattivatti.app.hue.domain.user.model.TokenSet
import java.time.Instant

data class TokenResponseDto(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("expires_in")
    val expiresIn: Long,
    @JsonProperty("refresh_token")
    val refreshToken: String
) {
    fun toTokenSet(currentTime: Instant) = TokenSet(
        accessToken = AccessToken(accessToken),
        accessTokenExpiresAt = currentTime.plusSeconds(expiresIn),
        refreshToken = RefreshToken(refreshToken)
    )
}
