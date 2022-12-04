package link.hattivatti.app.hue.application.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import link.hattivatti.app.hue.application.port.driven.ExchangeHueUserRefreshTokenForTokensPort
import link.hattivatti.app.hue.application.port.driven.ListHueUsersPort
import link.hattivatti.app.hue.application.port.driven.SaveHueUserPort
import link.hattivatti.app.hue.application.port.driving.RefreshAllHueUserTokensUseCase
import org.springframework.stereotype.Component

@Component
class HueUserTokenRefreshService(
    private val listHueUsersPort: ListHueUsersPort,
    private val exchangeHueUserRefreshTokenForTokensPort: ExchangeHueUserRefreshTokenForTokensPort,
    private val saveHueUsersPort: SaveHueUserPort
) : RefreshAllHueUserTokensUseCase {
    override suspend fun refreshAllHueUserTokens() = coroutineScope {
        val hueUsers = listHueUsersPort.listHueUsers()

        hueUsers
            .map {
                launch {
                    val newTokenSet = exchangeHueUserRefreshTokenForTokensPort
                        .exchangeHueUserRefreshTokenForTokens(it.tokens.refreshToken)

                    saveHueUsersPort.saveHueUser(it.copy(tokens = newTokenSet))
                }
            }
            .forEach { it.join() }
    }
}
