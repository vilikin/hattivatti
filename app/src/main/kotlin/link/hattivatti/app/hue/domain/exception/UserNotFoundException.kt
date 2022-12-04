package link.hattivatti.app.hue.domain.exception

import link.hattivatti.app.hue.domain.model.user.HueUserIdentifier

class UserNotFoundException(id: HueUserIdentifier) : RuntimeException("Hue user with ID ${id.id} does not exist")
