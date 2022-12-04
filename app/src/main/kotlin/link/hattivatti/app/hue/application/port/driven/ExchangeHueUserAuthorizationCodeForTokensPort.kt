package link.hattivatti.app.hue.application.port.driven

import link.hattivatti.app.hue.domain.user.model.AuthorizationCode
import link.hattivatti.app.hue.domain.user.model.TokenSet

interface ExchangeHueUserAuthorizationCodeForTokensPort {
    suspend fun exchangeHueUserAuthorizationCodeForTokens(authorizationCode: AuthorizationCode): TokenSet
}
