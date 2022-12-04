package link.hattivatti.app.hue.application.port.driven

import link.hattivatti.app.hue.domain.user.model.RefreshToken
import link.hattivatti.app.hue.domain.user.model.TokenSet

interface ExchangeHueUserRefreshTokenForTokensPort {
    suspend fun exchangeHueUserRefreshTokenForTokens(refreshToken: RefreshToken): TokenSet
}
