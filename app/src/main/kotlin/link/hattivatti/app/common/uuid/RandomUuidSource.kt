package link.hattivatti.app.common.uuid

import java.util.*

class RandomUuidSource : UuidSource {
    override fun uuid(): UUID {
        return UUID.randomUUID()
    }
}
