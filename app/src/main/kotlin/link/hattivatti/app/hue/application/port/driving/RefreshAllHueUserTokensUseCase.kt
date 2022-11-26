package link.hattivatti.app.hue.application.port.driving

interface RefreshAllHueUserTokensUseCase {
    // 1. Scan all records from HueUser table that have status REGISTERED
    // 2. For each user, issue token update
    fun refreshAllHueUserTokens()
}
