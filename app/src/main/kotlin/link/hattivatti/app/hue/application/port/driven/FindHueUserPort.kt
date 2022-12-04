package link.hattivatti.app.hue.application.port.driven

import link.hattivatti.app.hue.domain.model.user.HueUser
import link.hattivatti.app.hue.domain.model.user.HueUserIdentifier

interface FindHueUserPort {
    fun findHueUser(id: HueUserIdentifier): HueUser
}
