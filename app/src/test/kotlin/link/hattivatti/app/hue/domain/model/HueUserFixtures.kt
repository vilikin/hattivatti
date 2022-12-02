package link.hattivatti.app.hue.domain.model

import link.hattivatti.app.hue.domain.model.user.HueUser
import link.hattivatti.app.hue.domain.model.user.HueUserIdentifier
import link.hattivatti.app.hue.domain.model.user.HueUsername
import java.util.*

object HueUserFixtures {
    val user = HueUser(
        id = HueUserIdentifier(UUID.fromString("b59fd1a6-861f-4f27-a049-44612c4cf281")),
        username = HueUsername("testUsername"),
        tokens = TokenSetFixtures.tokenSet
    )
}
