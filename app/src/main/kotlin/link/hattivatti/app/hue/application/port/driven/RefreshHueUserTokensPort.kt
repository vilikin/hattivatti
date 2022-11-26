package link.hattivatti.app.hue.application.port.driven

import link.hattivatti.app.hue.domain.model.user.RefreshToken
import link.hattivatti.app.hue.domain.model.user.TokenSet

interface RefreshHueUserTokensPort {
    fun refreshHueUserTokens(refreshToken: RefreshToken): TokenSet
}
