package link.hattivatti.app.hue.domain.model.user

data class HueUser(
    val id: HueUserIdentifier,
    val status: HueUserStatus,
    val tokens: TokenSet
)
