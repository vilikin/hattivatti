package link.hattivatti.app.hue.application.port.driven

import link.hattivatti.app.hue.domain.user.model.HueUser

interface SaveHueUserPort {
    suspend fun saveHueUser(hueUser: HueUser)
}
