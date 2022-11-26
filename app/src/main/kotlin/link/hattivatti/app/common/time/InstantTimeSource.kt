package link.hattivatti.app.common.time

import java.time.Instant

interface InstantTimeSource {
    fun now(): Instant
}
