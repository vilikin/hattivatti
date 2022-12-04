package link.hattivatti.app.hue.domain.user.model

data class HueUser(
    val id: HueUserIdentifier,
    val username: HueUsername,
    val tokens: TokenSet
)
