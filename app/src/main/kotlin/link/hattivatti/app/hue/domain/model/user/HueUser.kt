package link.hattivatti.app.hue.domain.model.user

data class HueUser(
    val id: HueUserIdentifier,
    val username: HueUsername,
    val tokens: TokenSet
)
