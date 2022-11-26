package link.hattivatti.app.config

import link.hattivatti.app.common.time.CurrentInstantTimeSource
import link.hattivatti.app.common.time.InstantTimeSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InstantTimeSourceConfiguration {
    @Bean
    fun instantTimeSource(): InstantTimeSource {
        return CurrentInstantTimeSource()
    }
}
