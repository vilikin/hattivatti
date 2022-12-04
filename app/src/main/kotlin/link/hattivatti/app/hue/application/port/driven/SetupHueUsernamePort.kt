package link.hattivatti.app.hue.application.port.driven

import link.hattivatti.app.hue.domain.user.model.AccessToken
import link.hattivatti.app.hue.domain.user.model.HueUsername

interface SetupHueUsernamePort {
    suspend fun setupHueUsername(accessToken: AccessToken): HueUsername
}
