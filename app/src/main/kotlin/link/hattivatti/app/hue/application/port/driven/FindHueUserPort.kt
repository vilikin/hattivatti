package link.hattivatti.app.hue.application.port.driven

import link.hattivatti.app.hue.domain.user.model.HueUser
import link.hattivatti.app.hue.domain.user.model.HueUserIdentifier

interface FindHueUserPort {
    suspend fun findHueUser(id: HueUserIdentifier): HueUser
}
