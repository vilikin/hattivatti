package link.hattivatti.app.hue.application.port.driven

import link.hattivatti.app.hue.domain.user.model.HueUser

interface ListHueUsersPort {
    suspend fun listHueUsers(): List<HueUser>
}
