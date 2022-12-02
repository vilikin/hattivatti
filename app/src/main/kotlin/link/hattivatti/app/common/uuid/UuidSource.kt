package link.hattivatti.app.common.uuid

import java.util.UUID

interface UuidSource {
    fun uuid(): UUID
}
