package link.hattivatti.app.hue.application.port.driving

interface RegisterHueUserUseCase {
    // 1. Exchange authorization code to access & refresh tokens
    // 2. Save Hue user to DynamoDB table with tokens:
    //
    //    id | status | accessToken | accessTokenExpiresAt | refreshToken
    //           |
    //      [REGISTERED | DISABLED]
    //
    fun registerHueUser(authorizationCode: String)
}
