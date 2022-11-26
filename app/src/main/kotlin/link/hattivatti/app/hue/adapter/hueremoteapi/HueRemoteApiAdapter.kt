package link.hattivatti.app.hue.adapter.hueremoteapi

import link.hattivatti.app.hue.application.port.driven.ExchangeHueUserAuthorizationCodeForTokensPort
import link.hattivatti.app.hue.domain.model.user.AuthorizationCode
import link.hattivatti.app.hue.domain.model.user.TokenSet
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class HueRemoteApiAdapter(
    @Value("\${hue-remote-api.base-url}")
    private val baseUrl: String,

    @Value("\${hue-remote-api.client-id}")
    private val clientId: String,

    @Value("\${hue-remote-api.client-secret}")
    private val clientSecret: String,
) : ExchangeHueUserAuthorizationCodeForTokensPort {
    private val webClient = WebClient.builder().build()

    override suspend fun exchangeHueUserAuthorizationCodeForTokens(authorizationCode: AuthorizationCode): TokenSet {
        TODO("Not yet implemented")
    }
}
