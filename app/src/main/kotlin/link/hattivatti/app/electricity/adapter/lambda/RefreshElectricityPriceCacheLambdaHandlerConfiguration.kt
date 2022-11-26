package link.hattivatti.app.electricity.adapter.lambda

import kotlinx.coroutines.runBlocking
import link.hattivatti.app.electricity.application.port.driving.RefreshElectricityPriceCacheUseCase
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RefreshElectricityPriceCacheLambdaHandlerConfiguration(
    private val refreshElectricityPriceCacheUseCase: RefreshElectricityPriceCacheUseCase
) {
    private val logger = LoggerFactory.getLogger(RefreshElectricityPriceCacheLambdaHandlerConfiguration::class.java)

    @Bean
    fun refreshElectricityPriceCache(): () -> Unit = {
        runBlocking {
            logger.info("Refreshing electricity price cache")
            refreshElectricityPriceCacheUseCase.refreshElectricityPriceCache()
        }
    }
}
