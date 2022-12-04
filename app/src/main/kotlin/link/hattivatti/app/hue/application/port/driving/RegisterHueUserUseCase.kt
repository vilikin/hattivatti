package link.hattivatti.app.hue.application.port.driving

import link.hattivatti.app.hue.domain.user.model.AuthorizationCode
import link.hattivatti.app.hue.domain.user.model.HueUser

interface RegisterHueUserUseCase {
    // 1. Exchange authorization code to access & refresh tokens
    // 2. Save Hue user to DynamoDB table with tokens:
    //    id | accessToken | accessTokenExpiresAt | refreshToken | username
    // 3. Setup Hue username
    suspend fun registerHueUser(authorizationCode: AuthorizationCode): HueUser
}
