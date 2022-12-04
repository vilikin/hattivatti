package link.hattivatti.app.hue.application.service

import link.hattivatti.app.common.uuid.UuidSource
import link.hattivatti.app.hue.application.port.driven.ExchangeHueUserAuthorizationCodeForTokensPort
import link.hattivatti.app.hue.application.port.driven.SaveHueUserPort
import link.hattivatti.app.hue.application.port.driven.SetupHueUsernamePort
import link.hattivatti.app.hue.application.port.driving.RegisterHueUserUseCase
import link.hattivatti.app.hue.domain.user.model.AuthorizationCode
import link.hattivatti.app.hue.domain.user.model.HueUser
import link.hattivatti.app.hue.domain.user.model.HueUserIdentifier
import org.springframework.stereotype.Component

@Component
class HueUserRegistrationService(
    private val exchangeHueUserAuthorizationCodeForTokensPort: ExchangeHueUserAuthorizationCodeForTokensPort,
    private val setupHueUsernamePort: SetupHueUsernamePort,
    private val saveHueUserPort: SaveHueUserPort,
    private val uuidSource: UuidSource
) : RegisterHueUserUseCase {
    override suspend fun registerHueUser(authorizationCode: AuthorizationCode): HueUser {
        val tokens = exchangeHueUserAuthorizationCodeForTokensPort.exchangeHueUserAuthorizationCodeForTokens(
            authorizationCode
        )

        val username = setupHueUsernamePort.setupHueUsername(tokens.accessToken)

        val hueUser = HueUser(
            id = HueUserIdentifier(uuidSource.uuid()),
            username = username,
            tokens = tokens
        )

        saveHueUserPort.saveHueUser(hueUser)

        return hueUser
    }
}