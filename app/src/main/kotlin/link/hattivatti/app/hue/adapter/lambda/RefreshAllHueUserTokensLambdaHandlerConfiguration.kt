package link.hattivatti.app.hue.adapter.lambda

import kotlinx.coroutines.runBlocking
import link.hattivatti.app.hue.application.port.driving.RefreshAllHueUserTokensUseCase
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RefreshAllHueUserTokensLambdaHandlerConfiguration(
    private val refreshAllHueUserTokensUseCase: RefreshAllHueUserTokensUseCase
) {
    private val logger = LoggerFactory.getLogger(RefreshAllHueUserTokensLambdaHandlerConfiguration::class.java)

    @Bean
    fun refreshAllHueUserTokens(): () -> Unit = {
        runBlocking {
            logger.info("Refreshing all Hue user tokens")

            refreshAllHueUserTokensUseCase.refreshAllHueUserTokens()
        }
    }
}
