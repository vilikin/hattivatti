package link.hattivatti.app.config

import link.hattivatti.app.common.uuid.RandomUuidSource
import link.hattivatti.app.common.uuid.UuidSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UuidSourceConfiguration {
    @Bean
    fun uuidSource(): UuidSource {
        return RandomUuidSource()
    }
}
