package link.hattivatti.lambda

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HandlerConfig {
    @Bean
    fun handler(): () -> Unit {
        return {
            println("hello")
        }
    }
}
