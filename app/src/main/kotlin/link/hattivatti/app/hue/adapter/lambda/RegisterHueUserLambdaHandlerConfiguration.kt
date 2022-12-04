package link.hattivatti.app.hue.adapter.lambda

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import kotlinx.coroutines.runBlocking
import link.hattivatti.app.hue.application.port.driving.RegisterHueUserUseCase
import link.hattivatti.app.hue.domain.user.model.AuthorizationCode
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RegisterHueUserLambdaHandlerConfiguration(
    private val registerHueUserUseCase: RegisterHueUserUseCase
) {
    private val logger = LoggerFactory.getLogger(RegisterHueUserLambdaHandlerConfiguration::class.java)

    @Bean
    fun registerHueUser(): (request: APIGatewayProxyRequestEvent) -> APIGatewayProxyResponseEvent = {
        runBlocking {
            logger.info("Registering Hue user with given authorization code")

            val authorizationCodeString = it.queryStringParameters["code"]
                ?: throw IllegalArgumentException("Code must be provided as a query parameter")

            registerHueUserUseCase.registerHueUser(AuthorizationCode(authorizationCodeString))

            APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody("It workses!")
        }
    }
}
