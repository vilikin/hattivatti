package link.hattivatti.app.hue.adapter.hueremoteapi

import kotlinx.coroutines.reactive.awaitSingle
import link.hattivatti.app.common.time.InstantTimeSource
import link.hattivatti.app.hue.adapter.hueremoteapi.dto.TokenResponseDto
import link.hattivatti.app.hue.application.port.driven.ExchangeHueUserAuthorizationCodeForTokensPort
import link.hattivatti.app.hue.domain.model.user.AuthorizationCode
import link.hattivatti.app.hue.domain.model.user.TokenSet
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Component
class HueRemoteApiAdapter(
    @Value("\${hue-remote-api.base-url}")
    private val baseUrl: String,

    @Value("\${hue-remote-api.client-id}")
    private val clientId: String,

    @Value("\${hue-remote-api.client-secret}")
    private val clientSecret: String,

    private val instantTimeSource: InstantTimeSource,
) : ExchangeHueUserAuthorizationCodeForTokensPort {
    private val webClient = WebClient.builder()
        .baseUrl(baseUrl)
        .build()

    override suspend fun exchangeHueUserAuthorizationCodeForTokens(authorizationCode: AuthorizationCode): TokenSet {
        val formData = LinkedMultiValueMap<String, String>()
        formData.add("grant_type", "authorization_code")
        formData.add("code", authorizationCode.authorizationCode)

        return webClient.post()
            .uri("/v2/oauth2/token")
            .headers {
                it.setBasicAuth(clientId, clientSecret)
            }
            .body(BodyInserters.fromFormData(formData))
            .exchangeToMono {
                it.bodyToMono<TokenResponseDto>()
            }.map {
                it.toTokenSet(instantTimeSource.now())
            }
            .awaitSingle()
    }
}
