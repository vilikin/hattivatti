package link.hattivatti.app.hue.application.port.driven

import link.hattivatti.app.hue.domain.model.user.AuthorizationCode
import link.hattivatti.app.hue.domain.model.user.TokenSet

interface ExchangeHueUserAuthorizationCodeForTokensPort {
    suspend fun exchangeHueUserAuthorizationCodeForTokens(authorizationCode: AuthorizationCode): TokenSet
}
