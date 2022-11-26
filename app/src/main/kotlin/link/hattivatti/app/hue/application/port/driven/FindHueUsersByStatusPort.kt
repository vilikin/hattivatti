package link.hattivatti.app.hue.application.port.driven

import link.hattivatti.app.hue.domain.model.user.HueUser
import link.hattivatti.app.hue.domain.model.user.HueUserStatus

interface FindHueUsersByStatusPort {
    fun findHueUsersByStatus(status: HueUserStatus): List<HueUser>
}
