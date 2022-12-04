package link.hattivatti.app.hue.domain.user.exception

import link.hattivatti.app.hue.domain.user.model.HueUserIdentifier

class UserNotFoundException(id: HueUserIdentifier) : RuntimeException("Hue user with ID ${id.id} does not exist")
