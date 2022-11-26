package link.hattivatti.app.common.time

import java.time.Instant

class CurrentInstantTimeSource : InstantTimeSource {
    override fun now(): Instant {
        return Instant.now()
    }
}
