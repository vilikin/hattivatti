package link.hattivatti.app.hue.application.port.driven

import link.hattivatti.app.hue.domain.model.user.HueUser

interface SaveHueUserPort {
    suspend fun saveHueUser(hueUser: HueUser)
}
