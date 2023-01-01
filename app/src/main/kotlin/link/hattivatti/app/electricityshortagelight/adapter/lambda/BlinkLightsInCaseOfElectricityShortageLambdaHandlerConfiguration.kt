package link.hattivatti.app.electricityshortagelight.adapter.lambda

import kotlinx.coroutines.runBlocking
import link.hattivatti.app.electricityshortagelight.application.port.driving.BlinkLightsInCaseOfElectricityShortageUseCase
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BlinkLightsInCaseOfElectricityShortageLambdaHandlerConfiguration(
    private val blinkLightsInCaseOfElectricityShortageUseCase: BlinkLightsInCaseOfElectricityShortageUseCase
) {
    private val logger = LoggerFactory.getLogger(
        BlinkLightsInCaseOfElectricityShortageLambdaHandlerConfiguration::class.java
    )

    @Bean
    fun blinkLightsInCaseOfElectricityShortage(): () -> Unit = {
        runBlocking {
            logger.info("Blinking lights in case of electricity shortage")
            blinkLightsInCaseOfElectricityShortageUseCase.blinkLightsInCaseOfElectricityShortage()
        }
    }
}
