package link.hattivatti.app.hue.application.port.driven

import link.hattivatti.app.hue.domain.model.user.RefreshToken
import link.hattivatti.app.hue.domain.model.user.TokenSet

interface ExchangeHueUserRefreshTokenForTokensPort {
    suspend fun exchangeHueUserRefreshTokenForTokens(refreshToken: RefreshToken): TokenSet
}
