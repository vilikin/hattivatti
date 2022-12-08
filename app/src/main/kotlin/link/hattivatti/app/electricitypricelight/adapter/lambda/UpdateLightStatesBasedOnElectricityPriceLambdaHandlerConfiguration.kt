package link.hattivatti.app.electricitypricelight.adapter.lambda

import kotlinx.coroutines.runBlocking
import link.hattivatti.app.electricitypricelight.application.port.driving.UpdateLightStatesBasedOnElectricityPriceUseCase
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UpdateLightStatesBasedOnElectricityPriceLambdaHandlerConfiguration(
    private val updateLightStatesBasedOnElectricityPriceUseCase: UpdateLightStatesBasedOnElectricityPriceUseCase
) {
    private val logger = LoggerFactory.getLogger(
        UpdateLightStatesBasedOnElectricityPriceLambdaHandlerConfiguration::class.java
    )

    @Bean
    fun updateLightStatesBasedOnElectricityPrice(): () -> Unit = {
        runBlocking {
            logger.info("Updating light states based on electricity price")
            updateLightStatesBasedOnElectricityPriceUseCase.updateLightStates()
        }
    }
}
