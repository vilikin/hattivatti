package link.hattivatti.app.electricity.adapter.lambda

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import link.hattivatti.app.electricity.application.port.driving.ListElectricityPricesUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ListElectricityPricesLambdaHandlerConfiguration(
    private val listElectricityPricesUseCase: ListElectricityPricesUseCase,
    private val objectMapper: ObjectMapper
) {
    @Bean
    fun listElectricityPrices(): () -> APIGatewayProxyResponseEvent = {
        runBlocking {
            val electricityPrices = listElectricityPricesUseCase.listElectricityPrices()

            APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(
                    objectMapper.writeValueAsString(electricityPrices)
                )
        }
    }
}
