package link.hattivatti.app.prices.adapter.lambda

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UpdatePriceDataLambdaHandler {
    @Bean
    fun updatePriceDataLambdaHandler(): () -> Unit {
        return {
            println("Not doing anything yet")
        }
    }
}
