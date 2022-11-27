package link.hattivatti.app.hue.application.port.driven

import link.hattivatti.app.hue.domain.model.user.AccessToken
import link.hattivatti.app.hue.domain.model.user.HueUsername

interface SetupHueUsernamePort {
    suspend fun setupHueUsername(accessToken: AccessToken): HueUsername
}
