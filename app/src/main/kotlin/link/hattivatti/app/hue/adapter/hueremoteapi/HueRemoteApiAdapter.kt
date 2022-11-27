package link.hattivatti.app.hue.adapter.hueremoteapi

import kotlinx.coroutines.reactor.awaitSingle
import link.hattivatti.app.common.time.InstantTimeSource
import link.hattivatti.app.hue.adapter.hueremoteapi.dto.SuccessfulOperationDto
import link.hattivatti.app.hue.adapter.hueremoteapi.dto.TokenResponseDto
import link.hattivatti.app.hue.adapter.hueremoteapi.dto.UsernameResponseDto
import link.hattivatti.app.hue.application.port.driven.ExchangeHueUserAuthorizationCodeForTokensPort
import link.hattivatti.app.hue.application.port.driven.ExchangeHueUserRefreshTokenForTokensPort
import link.hattivatti.app.hue.application.port.driven.SetupHueUsernamePort
import link.hattivatti.app.hue.domain.model.user.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodilessEntity
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
) : ExchangeHueUserAuthorizationCodeForTokensPort,
    ExchangeHueUserRefreshTokenForTokensPort,
    SetupHueUsernamePort {

    companion object {
        private const val DEVICE_TYPE = "hattivatti"
    }

    private val webClient = WebClient.create(baseUrl)

    override suspend fun exchangeHueUserAuthorizationCodeForTokens(authorizationCode: AuthorizationCode): TokenSet {
        return getTokens(AuthorizationCodeGrant(authorizationCode))
    }

    override suspend fun exchangeHueUserRefreshTokenForTokens(refreshToken: RefreshToken): TokenSet {
        return getTokens(RefreshTokenGrant(refreshToken))
    }

    override suspend fun setupHueUsername(accessToken: AccessToken): HueUsername {
        executeUsernameSetupFirstStep(accessToken)
        return executeUsernameSetupSecondStep(accessToken)
    }

    private suspend fun getTokens(
        grant: Grant
    ): TokenSet {
        return webClient.post()
            .uri("/v2/oauth2/token")
            .headers {
                it.setBasicAuth(clientId, clientSecret)
            }
            .body(
                BodyInserters.fromFormData(
                    grant.getFormDataForTokenRequest()
                )
            )
            .exchangeToMono {
                it.bodyToMono<TokenResponseDto>()
            }.map {
                it.toTokenSet(instantTimeSource.now())
            }
            .awaitSingle()
    }

    private suspend fun executeUsernameSetupFirstStep(accessToken: AccessToken) {
        webClient.put()
            .uri("/route/api/0/config")
            .headers {
                it.setBearerAuth(accessToken.token)
                it.contentType = MediaType.APPLICATION_JSON
            }
            .body(BodyInserters.fromValue("""{"linkbutton":true}"""))
            .retrieve()
            .awaitBodilessEntity()
    }

    private suspend fun executeUsernameSetupSecondStep(accessToken: AccessToken): HueUsername {
        return webClient.post()
            .uri("/route/api")
            .headers {
                it.setBearerAuth(accessToken.token)
                it.contentType = MediaType.APPLICATION_JSON
            }
            .body(BodyInserters.fromValue("""{"devicetype":"$DEVICE_TYPE"}"""))
            .exchangeToMono {
                it.bodyToMono<List<SuccessfulOperationDto<UsernameResponseDto>>>()
            }
            .awaitSingle()
            .first()
            .let {
                HueUsername(it.success.username)
            }
    }

    private sealed interface Grant {
        fun getFormDataForTokenRequest(): LinkedMultiValueMap<String, String>
    }

    private class RefreshTokenGrant(val refreshToken: RefreshToken) : Grant {
        override fun getFormDataForTokenRequest(): LinkedMultiValueMap<String, String> {
            val formData = LinkedMultiValueMap<String, String>()
            formData.add("grant_type", "refresh_token")
            formData.add("refresh_token", refreshToken.token)
            return formData
        }
    }

    private class AuthorizationCodeGrant(val authorizationCode: AuthorizationCode) : Grant {
        override fun getFormDataForTokenRequest(): LinkedMultiValueMap<String, String> {
            val formData = LinkedMultiValueMap<String, String>()
            formData.add("grant_type", "authorization_code")
            formData.add("code", authorizationCode.authorizationCode)
            return formData
        }
    }
}
